package logic;

import common.Console;
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

    public ArrayList<Client> clients;

    public Crazy8s () {

        discard = new Deck(false);
        deck = new Deck(true);
        deck.shuffle();

        clients = new ArrayList<Client>();

        pickup_num = 1;

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
        massBroadcast("CURR_PLAYER" + current_player);
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
            return true;
        } else if (card.getSuit() == active_card.getSuit()) {
            return true;
        } else if (card.getRank() == active_card.getRank()) {
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

    public void nextPlayer () {
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

                    if (command.startsWith("MOVE")) {
                        byte suit = (byte)Character.getNumericValue(command.charAt(4));
                        byte rank = Byte.parseByte(command.substring(5));
                        Card played = new Card(suit, rank);

                        playCard(played);

                    } else if (command.startsWith("PICKUP")) {
                        pickUpCard();
                    } else if (command.startsWith("SKIP")) {
                        nextPlayer();
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
            giveCard(deck.get(0));
            hand.add(deck.get(0));
            deck.remove(0);
            notifyNumCard(this);
        }

        public void playCard (Card card) {
            if (isLegalMove(card)) {
                discard.add(active_card);
                active_card = card;

                notifyActiveCard();

                output.println("GOOD_MOVE");

                for (int i = 0; i < hand.size(); i ++) {
                    if (hand.get(i).equals(card)) {
                        hand.remove(i);
                    }
                }

                notifyNumCard(this);
                nextPlayer();
            } else {
                output.println("BAD_MOVE");
            }
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




