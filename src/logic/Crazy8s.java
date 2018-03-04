/**
 * Copyright 2018 (C) Jiawen Deng. All rights reserved.
 *
 * This document is the property of Jiawen Deng.
 * It is considered confidential and proprietary.
 *
 * This document may not be reproduced or transmitted in any form,
 * in whole or in part, without the express written permission of
 * Jiawen Deng.
 *
 *-----------------------------------------------------------------------------
 * Crazy8s.java
 *-----------------------------------------------------------------------------
 * This is an object that represents a round of Crazy8s game.
 * Because this is a local LAN multiplayer game, this object acts as
 * a server that makes all matchmaking decisions, as well as relaying these
 * information to each client.
 *-----------------------------------------------------------------------------
 */

package logic;

import common.Console;
import common.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.Timer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Crazy8s {

    private int current_player;         // number of current players
    private int pickup_num;             // number of cards to pick up (current)

    private Card active_card;           // the active card

    private Deck deck;                  // the deck
    private Deck discard;               // the discard pile

    private boolean game_started;       // boolean of whether the game had started

    /*
     * This is the keep alive timer;
     * every 2 seconds, the server "pings" the client
     * to notify them of the current connection status.
     * If the client do not detect ping, it will disconnect.
     */
    private Timer keep_alive;

    /*
     * As player clients connect, a server-side thread is
     * created to handle the incoming messages from that
     * particular client.
     * This arraylist holds a reference of all of these
     * threads.
     */
    private List<ClientThread> client_threads;

    /**
     * Default Constructor
     */
    public Crazy8s () {

        // initialize discard and deck piles
        // fill and shuffle deck
        discard = new Deck(false);
        deck = new Deck(true);
        deck.shuffle();

        // initialize thread arraylist
        client_threads = new CopyOnWriteArrayList<>();

        // initialize other variables
        pickup_num = 1;
        game_started = false;

        // start keep_alive timer
        keep_alive = new Timer(1000, e -> massBroadcast("ALIVE"));
        keep_alive.start();

    }

    /**
     * Method that determines if a winner is present.
     * @return boolean
     */
    public boolean hasWinner () {

        // if a client's hand is empty, broadcast winner status
        // and return true
        for (ClientThread client_thread : client_threads) {
            if (client_thread.hand.isEmpty()) {
                massBroadcast("WINNER" + client_thread.getNum());
                return true;
            }
        }

        // if deck is out of cards, find the player with the
        // least number of cards, and declare winner
        if (deck.size() == 0) {
            int winner = 0;

            for (ClientThread client_thread : client_threads) {
                if (client_thread.getNumCards() < client_threads.get(winner).getNumCards()) {
                    winner = client_thread.getNum();
                }
            }

            massBroadcast("WINNER" + winner);
            return true;
        }

        return false;
    }

    /**
     * Method that determines if a move is legal given
     * the current active card.
     * @param card  the card played
     * @return      boolean indicating if move is legal
     */
    public boolean isLegalMove (Card card) {

        // move is always legal if card is 8
        // else, compare suit and rank;
        // move is legal if equal

        if (card.getRank() == 8) {
            return true;
        } else if (card.getSuit() == active_card.getSuit()) {
            return true;
        } else if (card.getRank() == active_card.getRank()) {
            return true;
        }

        return false;
    }

    /**
     * Method that progresses the game onto the
     * next player.
     * @param isForceSkipped  whether a player had been skipped by a Jack
     */
    public void nextPlayer (boolean isForceSkipped) {

        // if a player had been force skipped,
        // broadcast message to let clients know,
        // then move on to next player

        if (isForceSkipped) {

            current_player += 1;

            if (current_player == client_threads.size()) {
                current_player = 0;
            }

            massBroadcast("FORCE_SKIP" + current_player);
        }

        current_player += 1;

        if (current_player == client_threads.size()) {
            current_player = 0;
        }

        // notify all clients of the current player
        notifyCurPlayer();
    }

    /**
     * Method to add a client thread for incoming
     * connection.
     * @param socket    socket of incoming connection
     */
    public void addClientThread (Socket socket) {
        ClientThread client = new ClientThread(socket);

        client_threads.add(client);

        client.start();
    }

    /**
     * Method that returns the number of clients.
     * @return  integer
     */
    public int getNumClient () {
        return client_threads.size();
    }

    /**
     * Method that broadcasts a message to all of
     * the clients via Sockets.
     * @param message   the message to be broadcasted
     */
    public void massBroadcast (String message) {

        for (ClientThread client_thread : client_threads) {
            client_thread.broadcast(message);
        }
    }

    /**
     * Method that broadcasts the current active cards
     * to all clients.
     */
    public void notifyActiveCard () {
        massBroadcast("ACTIVE_CARD" + active_card.getSuit() + active_card.getRank());
    }

    /**
     * Method that broadcasts a message when a new
     * player had joined or disconnected.
     * @param client    target client
     * @param connected whether the user connected or disconnected
     */
    public void notifyPlayerConnection (ClientThread client, boolean connected) {
        for (ClientThread client_thread : client_threads) {
            client_thread.updatePlayerConnection(client, connected);
        }
    }

    /**
     * Method that broadcasts the current player
     */
    public void notifyCurPlayer () {
        massBroadcast("CURR_PLAYER" + current_player);
    }

    /**
     * Method that notifies the change in readiness status of a client
     * @param client    target client
     * @param ready     whether the user is ready or not
     */
    public void notifyReadiness (ClientThread client, boolean ready) {
        for (ClientThread client_thread : client_threads) {
            client_thread.updateReadiness(client, ready);
        }
    }

    /**
     * Method that returns a boolean indicating if all players
     * are ready (so the game can start)
     * @return boolean
     */
    public boolean isAllReady () {

        // if all clients are ready, everyone is ready
        for (ClientThread client_thread : client_threads) {
            if (!client_thread.ready) return false;
        }
        return true;
    }

    /**
     * Method that broadcats the number of cards that a player has
     * @param player    target player
     */
    public void notifyNumCard (ClientThread player) {
        massBroadcast("NUM_CARD" + player.getNum() + player.hand.size());
    }

    /**
     * Method that starts the game.
     */
    public void startGame () {

        // broadcast a message notifying clients of the game starting
        massBroadcast("START_GAME");

        // give each client a subdeck of cards
        for (ClientThread client_thread : client_threads) {
            Deck subdeck = deck.subdeck(0, 7);
            subdeck.print();

            client_thread.giveHand(
                    Hand.valueOf(subdeck));

            // update the number of cards
            notifyNumCard(client_thread);
        }

        // get the first card in the deck as the active card
        // discard active card
        // notify active card
        active_card = deck.get(0);
        discard.add(active_card);
        deck.remove(active_card);
        notifyActiveCard();

        // get a random player to start the game
        current_player = ThreadLocalRandom.current().nextInt(0, client_threads.size());
        notifyCurPlayer();

    }

    /**
     * Method that stops the game.
     */
    public void stopGame () {

        for (ClientThread client_thread : client_threads) {
            client_thread.interrupt();
            client_threads.remove(client_thread);
        }
        keep_alive.stop();
    }

    /**
     * Nested class: ClientThread object (inheriting Thread object)
     * Creates a new thread to handle message from each incoming connection
     */
    public class ClientThread extends Thread {

        private Hand hand;              // player's hand
        private Socket socket;          // player's socket connection
        private BufferedReader input;   // for reading player's incoming messages
        private PrintWriter output;     // for writing message to player's client

        private String name;            // player's name

        private boolean interrupted;    // boolean of whether the thread had been interrupted
        private boolean ready;          // boolean of whether the player is ready to play

        /**
         * Constructor
         * @param socket   A player's incoming connection (socket)
         */
        public ClientThread (Socket socket) {

            // initialize socket connection IO

            this.socket = socket;

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                Console.printErrorMessage("Player died", this.getClass().getName());
            }

            ready = false;
            interrupted = false;
        }

        /**
         * Overloaded run method for the Thread
         */
        public void run () {

            Console.printGeneralMessage("Player " + getNum() + " connected.",
                    this.getClass().getName());

            // try & catch any IO errors
            try {

                // send welcome message, assign player number
                output.println("WELCOME" + getNum());
                output.println("ALIVE");

                // repeat loop until broken
                while (true) {

                    // if thread is interrupted, stop thread operation
                    if (interrupted) {return;}

                    // read messages sent from the client
                    String command = input.readLine();
                    Console.printGeneralMessage("CLIENT " + getNum() + " RESPONSE: " + command,
                            this.getClass().getName());

                    // if client response is null, means client had dropped connection
                    if (command == null) {
                        command = "QUIT";
                    }

                    if (!game_started) {
                        // if player is ready, update readiness
                        if (command.startsWith("READY")) {
                            notifyReadiness(this, true);
                            ready = true;

                            // if everyone is ready and more than 1 players are in the session
                            // stop listening for new players and start game
                            if (isAllReady() && client_threads.size() > 1) {
                                Server.stopListening();
                                startGame();
                                game_started = true;
                            }
                        } else if (command.startsWith("UNREADY")) {
                            notifyReadiness(this, false);
                            ready = false;
                        }

                        // if player request list of players, give list
                        // if player wish to update their name, update their name
                        if (command.startsWith("REQ_LIST")) {
                            giveList();
                        } else if (command.startsWith("NAME")) {
                            name = command.substring(4);
                            notifyPlayerConnection(this, true);
                        }
                    }

                    // if player played a 8 card, record original suit,
                    // the suit selected by the player, and the rank
                    if (command.startsWith("MOVE8")) {
                        byte original_suit = (byte)Character.getNumericValue(command.charAt(5));
                        byte selected_suit = (byte)Character.getNumericValue(command.charAt(7));
                        byte rank = (byte)Character.getNumericValue(command.charAt(6));
                        playEight(new Card(original_suit, rank), selected_suit);

                    // if player played a normal card,
                    // record the suit and rank
                    } else if (command.startsWith("MOVE")) {
                        byte suit = (byte)Character.getNumericValue(command.charAt(4));
                        byte rank = Byte.parseByte(command.substring(5));

                        playCard(new Card(suit, rank));

                    // if player requested pick up
                    } else if (command.startsWith("PICKUP")) {
                        pickUpCard();

                    // if player skipped turn
                    } else if (command.startsWith("SKIP")) {
                        nextPlayer(false);

                    // if player exited the session
                    // if game had started, broadcast an exception, and stop keeping session alive
                    } else if (command.startsWith("QUIT")) {
                        if (game_started) {
                            massBroadcast("EXCEPTION");
                            stopGame();

                        // if game had not started, remove the exited player only
                        } else {
                            interrupt();
                            client_threads.remove(this);
                            notifyPlayerConnection(this, false);
                        }
                    }
                }
            } catch (IOException e) {
                Console.printErrorMessage("A player had dropped connection.", this.getClass().getName());
            } finally {
                //at the end of sessions, close socket
                try {socket.close();} catch (IOException e) {}
            }
        }

        /**
         * Broadcast a message to the client
         * @param message   message to be broadcasted
         */
        public void broadcast (String message) {
            output.println(message);
        }

        /**
         * Update a player's connection status
         * if a player disconnected, update the client's number
         * (since numbering will change)
         * @param player     target player
         * @param connected  whether target player is connected
         */
        public void updatePlayerConnection (ClientThread player, boolean connected) {
            if (connected) output.println("CONNECT" + player.getNum() + player.name);
            else {
                output.println("DISCONNECT");
                output.println("NUM_UPDATE" + getNum());
            }
        }

        /**
         * Update the readiness status of a player
         * @param player    target player
         * @param ready     whether player is ready
         */
        public void updateReadiness (ClientThread player, boolean ready) {
            if (ready) output.println("READY" + player.getNum());
            else output.println("UNREADY" + player.getNum());
        }

        /**
         * Give the client a hand of cards
         * @param hand  the hand to be given
         */
        public void giveHand (Hand hand) {

            // give all cards in the hand, one at a time

            this.hand = hand;
            for (int i = 0; i < hand.size(); i ++) {
                giveCard(hand.get(i));
            }
        }

        /**
         * Give the player a card
         * @param card  card to be given
         */
        public void giveCard (Card card) {
            output.println("CARD" + card.getSuit() + card.getRank());
        }

        /**
         * Method that picks up a card
         */
        public void pickUpCard () {

            // if not enough cards in deck, move discard pile to deck
            if (deck.size() < pickup_num) {
                for (int i = 0; i < discard.size(); i ++) {
                    deck.add(discard.get(i));
                }
                for (int i = 0; i < discard.size(); i ++) {
                    discard.remove(i);
                }
            }

            // reshuffle the deck
            deck.shuffle();

            // try to get cards from the deck
            // catch exception if deck cards ran out
            // check winner status
            try {
                for (int i = 0; i < pickup_num; i++) {
                    giveCard(deck.get(0));
                    hand.add(deck.get(0));
                    deck.remove(0);
                    notifyNumCard(this);
                }
            } catch (IndexOutOfBoundsException e) {
                if (hasWinner()) {
                    stopGame();
                }
            }

            pickup_num = 1;
        }

        /**
         * Method that checks for special conditions
         * (eg Queen, Jack, and 2 etc.
         * @param card  the card to be checked
         */
        public void checkSpecialConditions (Card card) {

            // change pick up number accordingly

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

        /**
         * Method that plays a card
         * @param card  target card
         */
        public void playCard (Card card) {

            // check if move is legal
            // check special conditions

            if (isLegalMove(card)) {

                checkSpecialConditions(card);

                // discard card
                // set card as active card
                active_card = card;
                discard.add(card);

                // notify the new active card
                notifyActiveCard();

                // notify the client that move is good
                output.println("GOOD_MOVE");

                // remove card from the player's hand
                for (int i = 0; i < hand.size(); i ++) {
                    if (hand.get(i).equals(card)) {
                        hand.remove(i);
                    }
                }

                // update player's number of cards
                notifyNumCard(this);

                // if card is a jack, next player is skipped
                // (this is not checked in special conditions)
                if (card.getRank() == Constants.JACK) {
                    nextPlayer(true);
                } else {
                    nextPlayer(false);
                }

                // if there is a winner, stop game
                if (hasWinner()) {
                    stopGame();
                }

            } else {
                // if move is not good, inform player of bad move
                output.println("BAD_MOVE");
            }
        }

        /**
         * Method that plays a 8
         * @param card  target card
         * @param suit  the player's chosen suit
         */
        public void playEight (Card card, byte suit) {

            // the eight card is still discarded, but the active
            // card's suit is chosen by the player
            discard.add(card);
            active_card = new Card (suit, card.getRank());

            // rest of the method is the same as
            // playCard (Card card)
            notifyActiveCard();

            output.println("GOOD_MOVE");

            for (int i = 0; i < hand.size(); i ++) {
                if (hand.get(i).equals(card)) {
                    hand.remove(i);
                }
            }

            notifyNumCard(this);
            nextPlayer(false);

            if (hasWinner()) {
                stopGame();
            }
        }

        /**
         * Returns the number assigned to the player
         * @return integer
         */
        public int getNum () {
            return client_threads.indexOf(this);
        }

        /**
         * Returns the number of cards that player has
         * @return integer
         */
        public int getNumCards () {
            return hand.size();
        }

        /**
         * Give client the list of players
         */
        public void giveList () {
            for (ClientThread client_thread : client_threads) {
                updatePlayerConnection(client_thread, true);
                if (client_thread.ready) {
                    output.println("READY" + client_thread.getNum());
                }
            }
        }

        /**
         * Interrupt the thread
         */
        public void interrupt () {
            interrupted = true;

            try {
                socket.close();
                input.mark(0);
                input.reset();
                input.close();

                output.flush();
                output.close();
            } catch (IOException e) {}
        }

    }


}




