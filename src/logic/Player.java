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
 * Player.java
 *-----------------------------------------------------------------------------
 * This class serves as a client for the socket server, as well as an object
 * that tracks all the variables relevant to the player.
 *-----------------------------------------------------------------------------
 */

package logic;

import common.Console;
import ui.*;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Player {

    private static WeakReference<HandPanel> hand_panel;     // weak reference of hand panel
    private static WeakReference<GamePanel> panel;          // weak reference of game panel
    private static WeakReference<WinnerPanel> winner;       // weak reference of winner panel
    private static WeakReference<GameWindow> frame;         // weak reference of game window

    private Socket socket;      // socket for interacting with server
    private BufferedReader in;  // for reading server messages
    private PrintWriter out;    // for sending messages to server

    private Hand hand;              // the player's hand
    private String name;            // the player's name
    private boolean game_started;   // boolean of whether the game had started
    private int player_number;      // the player's assigned number (assigned by server)

    private Timer keep_alive;       // timer to check server's "keep alive" messages to ensure connections
    private boolean isAlive;        // boolean updated by the keep alive timer

    private HashMap <Integer, String> opponents;    // a hashmap of all opponents, incl. their number and name

    private Card active_card;       // the current active card

    private ExecutorService player_thread = Executors.newSingleThreadExecutor();    // a seperate thread to run the client code

    /**
     * Default Constructor
     * @param server_address address of server
     * @param port  port
     * @param name  player name
     */
    public Player(String server_address, int port, String name) {

        try {
            socket = new Socket(server_address, port);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            Console.printErrorMessage("A connection cannot be established.", this.getClass().getName());
            References.unexpectedDisconnect(false);
        }

        this.opponents = new HashMap<>();

        this.hand = new Hand();
        this.active_card = null;
        this.name = name;
        this.isAlive = true;
        this.game_started = false;

        /*
         * the keep alive timer checks if the server had pinged
         * the client every 5 seconds.
         * server should ping every 2 seconds.
         * if ping is not received, assume connection
         * to server is lost.
         */
        keep_alive = new Timer(5000, e -> {
            if (isAlive) {
                isAlive = false;
            } else {
                player_thread.shutdownNow();
                keep_alive.stop();
                References.unexpectedDisconnect(true);
            }
        });

        References.updateReferences();

        // submit the play() method to the thread
        player_thread.submit(this::play);
    }

    /**
     * Method that returns the player's hand
     * @return  player's hand
     */
    public Hand getHand () {
        return hand;
    }

    /**
     * Method that returns the player's number
     * @return  integer of player's number
     */
    public int getNum () {
        return player_number;
    }

    /**
     * Method that stops the keep alive timer.
     */
    public void stopKeepAlive () {
        keep_alive.stop();

        try {
            socket.close();
            out.flush();
            out.close();
            in.mark(0);
            in.reset();
            in.close();
        } catch (IOException e) {}
    }

    /**
     * Method that updates the list of opponents
     */
    public void updateList () {
        opponents.clear();
        out.println("REQ_LIST");
    }

    /**
     * Method that votes to start the game.
     */
    public void voteToStart () {
        out.println("READY");
    }

    /**
     * Method that votes not to start the game.
     */
    public void unvoteToStart () {
        out.println("UNREADY");
    }

    /**
     * Method that requests a card pick up.
     */
    public void pickUp () {
        out.println("PICKUP");
    }

    /**
     * Method that skips the player's turn.
     */
    public void skip () {
        out.println("SKIP");
    }

    /**
     * Method that plays the user's selected card
     */
    public void playCard () {
        if (hand.getSelectedCard().getRank() == 8) {
            frame.get().setEightPanel(true);
            return;
        }
        out.println("MOVE" + hand.getSelectedCard().getSuit() + hand.getSelectedCard().getRank());
    }

    /**
     * Special method to play a 8 card, with the user's chosen suit.
     * @param suit  the user's chosen suit
     */
    public void playEight (int suit) {
        out.println("MOVE8" + hand.getSelectedCard().getSuit() + hand.getSelectedCard().getRank() + suit);
    }

    /**
     * Method that returns the opponent list.
     * @return  HashMap of opponents
     */
    public HashMap <Integer, String> getPlayerList () {
        return opponents;
    }

    /**
     * Contains a loop that runs when game is running
     */
    public void play() {

        // start keep alive timer
        keep_alive.start();
        String response;

        try {

            while (true) {

                // read incoming messages
                try {
                    response = in.readLine();
                } catch (IOException e) {
                    References.unexpectedDisconnect(true);
                    break;
                }

                System.out.println(response);

                // check alive and exception messages
                if (response.startsWith("ALIVE")) isAlive = true;
                if (response.startsWith("EXCEPTION")) {
                    isAlive = false;
                }

                Console.printGeneralMessage(response, this.getClass().getName());

                // if game had not started, check for readiness status
                // and connect/disconnect updates
                if (!game_started) {
                    checkCoreResponse(response);
                    checkReadiness(response);

                // otherwise, check move messages and other
                // game related messages
                } else {
                    checkMoveResponse(response);
                    checkGameResponse(response);
                }

            }

            // if loop is broken, send quit message to server
            out.println("QUIT");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }

    /**
     * Method that checks all messages related to voting to start
     * @param response  server messages
     */
    public void checkReadiness (String response) {

        // update player's readiness status in their playerPanel if they changed their vote
        // (playerPanel.voteStart(boolean))

        if (response.startsWith("READY")) {
            int num = Integer.parseInt(response.substring(5));
            if (num != player_number) {
                SwingUtilities.invokeLater(() -> {
                    PlayerPanel.getPanel(num).voteStart(true);
                });
            }
        } else if (response.startsWith("UNREADY")) {
            int num = Integer.parseInt(response.substring(7));
            if (num != player_number) {
                PlayerPanel.getPanel(num).voteStart(false);
            }
        }
    }

    /**
     * Method that checks all messages related to moves
     * @param response  server messages
     */
    public void checkMoveResponse (String response) {

        // if moove is good, remove selected card, update hand, stop turn
        // otherwise, do nothing. (was going to implement a message, but ran out of time)

        if (response.startsWith("GOOD_MOVE")) {
            hand.remove(hand.getSelectedIndex());
            hand.reset();
            SwingUtilities.invokeLater(() -> panel.get().stopPlay());
        } else if (response.startsWith("BAD_MOVE")) {}
    }

    /**
     * Method that checks all core responses (connect, disconnect, welcome, etc)
     * @param response  server messages
     */
    public void checkCoreResponse (String response) {

        // if server respond with welcome, connecton had been established.
        // respond with player name and request the list of players

        // if server wishes to update the player number, record it locally.

        // if a new player connects, put them in the hashmap and create a new playerpanel.
        // if a player disconnects, reset all players and request list again

        // if game started, update boolean and GUI accordingly

        if (response.startsWith("WELCOME")) {
            player_number = Character.getNumericValue(response.charAt(7));
            out.println("NAME" + name);
            out.println("REQ_LIST");
        } else if (response.startsWith("NUM_UPDATE")) {
            player_number = Character.getNumericValue(response.charAt(10));
        } else if (response.startsWith("CONNECT")) {
            int num = Integer.parseInt(response.substring(7, 8));
            String playerName = response.substring(8);

            if (num != player_number) {
                opponents.put(num, playerName);
            }

            SwingUtilities.invokeLater(() -> panel.get().refreshPlayers());
        } else if (response.startsWith("DISCONNECT")) {
            if (game_started) References.unexpectedDisconnect(false);
            else SwingUtilities.invokeLater(() -> panel.get().resetPlayers());
        }

        if (response.startsWith("START_GAME")) {
            SwingUtilities.invokeLater(() -> panel.get().startGame());
            game_started = true;
        }
    }

    /**
     * Method that checks all game-related responses
     * @param response  server messages
     */
    public void checkGameResponse (String response) {

        // ACTIVE_CARD: update active card
        // CARD: add a card to player's hand
        // NUM_CARD: update the number of cards that a player has
        // FORCE_SKIP: skipped by a Jack (planning to implement messages, but ran out of time)
        // WINNER: signals game over, delivers name of the winner

        if (response.startsWith("ACTIVE_CARD")) {
            byte suit = Byte.parseByte(response.substring(11, 12));
            byte rank = Byte.parseByte(response.substring(12));
            Console.print("ACTIVE: suit " + suit + " rank " + rank);
            SwingUtilities.invokeLater(() -> panel.get().updateActiveCard(new Card(suit, rank)));
        } else if (response.startsWith("CARD")) {
            byte suit = Byte.parseByte(response.substring(4, 5));
            byte rank = Byte.parseByte(response.substring(5));
            hand.add(new Card(suit, rank));
            if (active_card != null) {
                hand.findPlayable();
            }
            SwingUtilities.invokeLater(() -> hand_panel.get().updateLayout());
        } else if (response.startsWith("NUM_CARD")) {
            int num = Character.getNumericValue(response.charAt(8));
            int numCard = Integer.parseInt(response.substring(9));
            Console.print("NUM = " + num + " NUMCARD = " + numCard);
            if (num != player_number)
                SwingUtilities.invokeLater(() -> PlayerPanel.getPanel(num).updateNumCard(numCard));
        } else if (response.startsWith("FORCE_SKIP")) {
            int num = Character.getNumericValue(response.charAt(10));
            if (num == player_number) {
            }
        } else if (response.startsWith("WINNER")) {
            int num = Character.getNumericValue(response.charAt(6));
            if (num == player_number) SwingUtilities.invokeLater(() -> {
                winner.get().youWon();
                panel.get().stopPlay();

                keep_alive.stop();
            });
            else SwingUtilities.invokeLater(() -> {
                winner.get().otherWon(PlayerPanel.getPanel(num).getName());
                panel.get().stopPlay();

                keep_alive.stop();
            });
        }

        // CURR_PLAYER: updates the player number of the current player
        if (response.startsWith("CURR_PLAYER")) {
            int num = Character.getNumericValue(response.charAt(11));
            if (num != player_number) SwingUtilities.invokeLater(() -> panel.get().updateCurrentPlayer(num));
            else {
                SwingUtilities.invokeLater(() -> panel.get().startPlay());
            }
        }
    }

    /**
     * Method that returns the list of opponents.
     * @return the map of opponents
     */
    public HashMap<Integer, String> getOpponents () {
        return opponents;
    }

    /**
     * Method that returns the current active card
     * @return  the active card
     */
    public Card getActiveCard () {
        return active_card;
    }

    /**
     * Method that sets the current active card
     * @param active_card   the active card
     */
    public void setActiveCard (Card active_card) {
        this.active_card = active_card;
    }

    /**
     * Updates all weak references
     */
    public static void updateReferences () {
        frame = new WeakReference<>(References.frame);
        hand_panel = new WeakReference<>(References.hand);
        panel = new WeakReference<>(References.panel);
        winner = new WeakReference<>(References.winner);
    }

}




