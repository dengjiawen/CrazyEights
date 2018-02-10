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
            playerCards[i] = (Hand) deck.subdeck(i, i + 8);
            for (int j = 0; j < playerCards[i].size(); j++) {
                deck.remove(playerCards[i].get(j));
            }
            Server.players.get(i).giveHand(playerCards[i]);
        }
        activeCard = deck.get(0);
    }

    public void updatePlayerList (Client client, boolean connected) {
        for (int i = 0; i < Server.players.size(); i ++) {
            Server.players.get(i).updatePlayerList(client, client.getNum(), connected);
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

        public Client (Socket socket) {

            this.socket = socket;

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                Console.print("Player died");
            }
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
                        if (numReady == Server.players.size() && Server.players.size() > 1) {
                            Server.listener.close();
                            startGame();
                        }
                    }
                    if (command.startsWith("NAME")) {
                        playerName = command.substring(4);
                        Console.print(playerName);
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
                        Server.game.updatePlayerList(this, false);
                        Server.players.remove(this);
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }

        public void updatePlayerList (Client player, int playerNum, boolean connected) {
            if (connected) output.println("CONNECT" + playerNum + player.playerName);
            else {
                output.println("DISCONNECT" + playerNum);
                output.println("NUM_UPDATE" + getNum());
            }
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
                Crazy8s.this.updatePlayerList(Server.players.get(i), true);
            }
        }

    }


}




