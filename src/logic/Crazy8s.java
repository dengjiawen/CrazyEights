package logic;

import common.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Crazy8s {

    public int currentPlayer;

    public Card activeCard;

    Hand[] playerCards;

    Deck deck;
    Deck discard;

    int numReady;

    public Crazy8s () {
        deck = new Deck(true);
        deck.shuffle();

        discard = new Deck(false);

        numReady = 0;
    }

    public void startGame () {
        playerCards = new Hand[Server.players.size()];

        for (int i = 0; i < playerCards.length; i += 9) {
            Deck temp_subdeck = deck.subdeck(i, i + 8);
            for (int j = 0; j < temp_subdeck.size(); j ++) {
                playerCards[i].add(temp_subdeck.get(j));
                deck.remove(playerCards[i].get(j));
            }
            Server.players.get(i).giveHand(playerCards[i]);
        }
        activeCard = deck.get(0);
    }

    public void updatePlayerList (Client client, boolean connected) {
        for (int i = 0; i < Server.players.size(); i ++) {
            Server.players.get(i).updatePlayerList(client, connected);
        }
    }

    public void updateReadiness (Client client, boolean ready) {
        for (int i = 0; i < Server.players.size(); i ++) {
            Server.players.get(i).updateReadiness(client, ready);
        }
    }

    public boolean hasWinner () {
        for (int i = 0; i < playerCards.length; i++) {
            if (playerCards[i].size() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isLegalMove (Card card) {
        if (card.getRank() == 8) {
            return true;
        } else if (card.getSuit() == activeCard.getSuit()) {
            return true;
        } else if (card.getRank() == activeCard.getRank()) {
            return true;
        }

        return false;
    }

    public class Client extends Thread {

        String playerName;

        Socket socket;
        BufferedReader input;
        PrintWriter output;

        boolean ready;

        public Client (Socket socket) {

            this.socket = socket;

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                Console.print("Player died");
            }

            ready = false;
        }

        public void run () {

            Console.print("Player " + getNum() + " connected.");

            try {

                if (getNum() == currentPlayer) {
                    output.println("YOUR_MOVE");
                }

                output.println("WELCOME" + getNum());

                while (true) {
                    String command = input.readLine();
                    if (command == null) {
                        command = "QUIT";
                    }

                    if (command.startsWith("REQ_LIST")) {
                        giveList();
                    }

                    if (command.startsWith("READY")) {
                        numReady ++;
                        ready = true;
                        if (numReady == Server.players.size() && Server.players.size() > 1) {
                            Server.listener.close();
                            startGame();
                        }
                        Server.game.updateReadiness(this, true);
                    } else if (command.startsWith("UNREADY")) {
                        numReady --;
                        ready = false;
                        Server.game.updateReadiness(this, false);
                    }


                    if (command.startsWith("NAME")) {
                        playerName = command.substring(4);
                        Server.game.updatePlayerList(this, true);
                    }
                    if (command.startsWith("MOVE")) {
                        byte suit = (byte)Character.getNumericValue(command.charAt(4));
                        byte rank = Byte.parseByte(command.substring(5));
                        Card played = new Card(suit, rank);

                        if (isLegalMove(played)) {
                            discard.add(activeCard);
                            activeCard = played;
                            currentPlayer += 1;
                            output.println("GOOD_MOVE");
                        } else {
                            output.println("BAD_MOVE");
                        }

                    } else if (command.startsWith("QUIT")) {
                        Server.players.remove(this);
                        Server.game.updatePlayerList(this, false);
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }

        public void updatePlayerList (Client player, boolean connected) {
            if (connected) output.println("CONNECT" + player.getNum() + player.playerName);
            else {
                output.println("DISCONNECT");
                output.println("NUM_UPDATE" + getNum());
            }
        }

        public void updateReadiness (Client player, boolean ready) {
            if (ready) output.println("READY" + player.getNum());
            else output.print("UNREADY" + player.getNum());
        }

        public void giveHand (Hand hand) {
            for (int i = 0; i < hand.size(); i ++) {
                output.println("CARD" + hand.get(i).getSuit() + hand.get(i).getRank());
            }
        }

        public void giveCard (Card card) {
            output.println("CARD" + card.getSuit() + card.getRank());
        }

        public int getNum () {
            return Server.players.indexOf(this);
        }

        public void giveList () {
            for (int i = 0; i < Server.players.size(); i ++) {
                updatePlayerList(Server.players.get(i), true);
                if (Server.players.get(i).ready == true) {
                    output.println("READY" + Server.players.get(i).getNum());
                }
            }
        }

    }


}




