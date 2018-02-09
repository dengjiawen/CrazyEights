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

    public int numPlayer;
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
        playerCards = new Hand[numPlayer];

        for (int i = 0; i < playerCards.length; i += 9) {
            playerCards[i] = (Hand) deck.subdeck(i, i + 8);
            for (int j = 0; j < playerCards[i].size(); j++) {
                deck.remove(playerCards[i].get(j));
            }
        }
        activeCard = deck.get(0);
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

        int playerNum;
        String playerName;

        Socket socket;
        BufferedReader input;
        PrintWriter output;

        public Client (Socket socket, int playerNum) {

            Console.print("Player " + playerNum + " connected.");

            this.socket = socket;
            this.playerNum = playerNum;

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                output.println("WELCOME" + playerNum);

            } catch (IOException e) {
                Console.print("Player died");
            }
        }

        public void run () {
            try {

                // Tell the first player that it is his/her turn.
                if (playerNum == currentPlayer) {
                    output.println("YOUR_MOVE");
                }

                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("READY")) {
                        numReady ++;
                        if (numReady == playerNum) {
                            Server.listener.close();
                            startGame();
                        }
                    }
                    if (command.startsWith("NAME")) {
                        playerName = command.substring(4);
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
            if (connected) output.println("CONNECT" + player.playerNum + player.playerName);
            else output.println("DISCONNECT" + player.playerNum);
        }

        public void addCard () {

        }

    }


}




