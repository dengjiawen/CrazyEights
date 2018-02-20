package logic;

import common.Console;
import common.Constants;
import common.Misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Crazy8s {

    private int current_player;
    private int pickup_num;

    private Card active_card;

    private Deck deck;
    private Deck discard;

    private boolean active_skipped;

    public ArrayList<Client> clients;

    public Crazy8s () {

        discard = new Deck(false);
        deck = new Deck(true);
        deck.shuffle();

        clients = new ArrayList<Client>();

        pickup_num = 1;
        active_skipped = false;

    }

    public void startGame () {

        massBroadcast("START_GAME");

        for (int i = 0; i < clients.size(); i++) {

            Deck subdeck = deck.subdeck(0, 7);
            subdeck.print();

            clients.get(i).giveHand(
                    Hand.valueOf(subdeck));
            notifyNumCard(clients.get(i));
        }

        active_card = deck.get(0);
        discard.add(active_card);
        deck.remove(active_card);
        notifyActiveCard();

        current_player = ThreadLocalRandom.current().nextInt(0, clients.size());
        notifyCurPlayer();

    }

    public void massBroadcast (String message) {
        for (int i = 0; i < clients.size(); i ++) {
            clients.get(i).broadcast(message);
        }
    }

    public void notifyActiveCard () {
        massBroadcast("ACTIVE_CARD" + active_card.getSuit() + active_card.getRank());
    }

    public void notifyList (Client client, boolean connected) {
        for (int i = 0; i < clients.size(); i ++) {
            clients.get(i).updatePlayerList(client, connected);
        }
    }

    public void notifyCurPlayer () {
        massBroadcast("CURR_PLAYER" + current_player + (active_skipped ? 1 : 0));
    }

    public void notifyReadiness (Client client, boolean ready) {
        for (int i = 0; i < clients.size(); i ++) {
            clients.get(i).updateReadiness(client, ready);
        }
    }

    public void notifyNumCard (Client player) {
        massBroadcast("NUM_CARD" + player.getNum() + player.hand.size());
    }

    public boolean hasWinner () {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).hand.size() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isLegalMove (Card card) {

        if (card.getRank() == 8) {
            active_skipped = false;
            return true;
        } else if (card.getSuit() == active_card.getSuit()) {
            active_skipped = false;
            return true;
        } else if (card.getRank() == active_card.getRank()) {
            active_skipped = false;
            return true;
        }

        if (active_skipped) {
            active_skipped = false;
            return true;
        }

        return false;
    }

    public boolean isAllReady () {
        for (int i = 0; i < clients.size(); i ++) {
            if (!clients.get(i).ready) return false;
        }
        return true;
    }

    public void nextPlayer (boolean forceSkipped) {

        if (forceSkipped) {
            massBroadcast("FORCE_SKIP" + current_player);
            return;
        }

        current_player += 1;

        if (current_player == clients.size()) {
            current_player = 0;
        }

        notifyCurPlayer();
    }

    public class Client extends Thread {

        private Hand hand;
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        private String name;

        private boolean ready;

        public Client (Socket socket) {

            this.socket = socket;

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                Console.printErrorMessage("Player died", this.getClass().getName());
            }

            ready = false;
        }

        public void run () {

            Console.printGeneralMessage("Player " + getNum() + " connected.",
                    this.getClass().getName());

            try {

                output.println("WELCOME" + getNum());

                while (true) {

                    String command = input.readLine();
                    Console.printGeneralMessage("CLIENT " + getNum() + " RESPONSE: " + command,
                            this.getClass().getName());

                    if (command == null) {
                        command = "QUIT";
                    }

                    if (command.startsWith("REQ_LIST")) {
                        giveList();
                    } else if (command.startsWith("NAME")) {
                        name = command.substring(4);
                        notifyList(this, true);
                    }

                    if (command.startsWith("READY")) {
                        notifyReadiness(this, true);

                        ready = true;
                        if (isAllReady() && clients.size() > 1) {
                            Server.stopListening();
                            startGame();
                        }
                    } else if (command.startsWith("UNREADY")) {
                        notifyReadiness(this, false);
                        ready = false;
                    }

                    if (command.startsWith("MOVE8")) {
                        byte original_suit = (byte)Character.getNumericValue(command.charAt(5));
                        byte selected_suit = (byte)Character.getNumericValue(command.charAt(7));
                        byte rank = (byte)Character.getNumericValue(command.charAt(6));
                        playEight(new Card(original_suit, rank), selected_suit);
                    } else if (command.startsWith("MOVE")) {
                        byte suit = (byte)Character.getNumericValue(command.charAt(4));
                        byte rank = Byte.parseByte(command.substring(5));
                        Card played = new Card(suit, rank);

                        playCard(played);

                    } else if (command.startsWith("PICKUP")) {
                        pickUpCard();
                    } else if (command.startsWith("SKIP")) {
                        //active_skipped = true;
                        nextPlayer(false);

                    } else if (command.startsWith("QUIT")) {
                        clients.remove(this);
                        notifyList(this, false);
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {e.printStackTrace();}
            }
        }

        public void broadcast (String message) {
            output.println(message);
        }

        public void updatePlayerList (Client player, boolean connected) {
            if (connected) output.println("CONNECT" + player.getNum() + player.name);
            else {
                output.println("DISCONNECT");
                output.println("NUM_UPDATE" + getNum());
            }
        }

        public void updateReadiness (Client player, boolean ready) {
            if (ready) output.println("READY" + player.getNum());
            else output.println("UNREADY" + player.getNum());
        }

        public void giveHand (Hand hand) {
            this.hand = hand;
            for (int i = 0; i < hand.size(); i ++) {
                giveCard(hand.get(i));
            }
        }

        public void giveCard (Card card) {
            output.println("CARD" + card.getSuit() + card.getRank());
        }

        public void pickUpCard () {

            if (deck.size() < pickup_num) {
                for (int i = 0; i < discard.size(); i ++) {
                    deck.add(discard.get(i));
                }
                for (int i = 0; i < discard.size(); i ++) {
                    discard.remove(i);
                }
            }

            deck.shuffle();

            for (int i = 0; i < pickup_num; i ++) {
                giveCard(deck.get(0));
                hand.add(deck.get(0));
                deck.remove(0);
                notifyNumCard(this);
            }

            pickup_num = 1;
        }

        public void checkSpecialConditions (Card card) {

            if (card.getRank() == 2) {
                if (pickup_num == 1) pickup_num = 2;
                else pickup_num += 2;
            } else if (card.getSuit() == Constants.SPADES && card.getRank() == Constants.QUEEN) {
                if (pickup_num == 1) pickup_num = 5;
                else pickup_num += 5;
            } else {
                pickup_num = 1;
            }

        }

        public void playCard (Card card) {

            if (isLegalMove(card)) {

                checkSpecialConditions(card);

                active_card = card;
                discard.add(card);

                notifyActiveCard();

                output.println("GOOD_MOVE");

                for (int i = 0; i < hand.size(); i ++) {
                    if (hand.get(i).equals(card)) {
                        hand.remove(i);
                    }
                }

                notifyNumCard(this);
                nextPlayer(false);

                if (card.getRank() == Constants.JACK) {
                    nextPlayer(true);
                }

            } else {
                output.println("BAD_MOVE");
            }
        }

        public void playEight (Card card, byte suit) {
            discard.add(card);
            active_card = new Card (suit, card.getRank());

            notifyActiveCard();

            output.println("GOOD_MOVE");

            for (int i = 0; i < hand.size(); i ++) {
                if (hand.get(i).equals(card)) {
                    hand.remove(i);
                }
            }

            notifyNumCard(this);
            nextPlayer(false);
        }

        public int getNum () {
            return clients.indexOf(this);
        }

        public void giveList () {
            for (int i = 0; i < clients.size(); i ++) {
                updatePlayerList(clients.get(i), true);
                if (clients.get(i).ready) {
                    output.println("READY" + clients.get(i).getNum());
                }
            }
        }

    }


}




