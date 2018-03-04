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
 * GamePanel.java
 *-----------------------------------------------------------------------------
 * This panel hosts all of the gaming graphics.
 *-----------------------------------------------------------------------------
 */

package ui;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import common.Constants;
import logic.Card;
import logic.Player;

public class GamePanel extends JPanel {

    private static WeakReference <ArrayList<PlayerPanel>> players;  // weak reference of playerpanels
    private static WeakReference <HandPanel> hand;                  // weak reference of hand panel
    private static WeakReference <ButtonPanel> buttons;             // weak reference of buttons panel
    private static WeakReference <StackPanel> stack;                // weak reference of stacks panel

    private static WeakReference <GameWindow> frame;      // weak reference of main game window
    private static WeakReference <Player> player;         // weak reference of player object

    private static int width = Constants.getInt("initWidth");   // panel width
    private static int height = Constants.getInt("initHeight"); // panel height

    int current_player = Constants.DEFAULT;   // current player

    public GamePanel () {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);

        // initialize all daughter JComponents

        References.buttons = new ButtonPanel();
        References.stack = new StackPanel();

        References.players = new ArrayList<>();
        References.players.add(null);
        References.players.add(null);
        for (int i = 2; i <= Constants.getInt("MaxPlayer"); i ++) {
            References.players.add(new PlayerPanel(0, i));
            References.players.get(i).setVisible(false);
            add(References.players.get(i));
        }

        //update all references
        References.updateReferences();

        add(References.buttons);
        add(References.stack);

    }

    /**
     * Initialize panel
     */
    public void init () {
        buttons.get().setVisible(true);
        buttons.get().setVoteStart(true);
    }

    /**
     * Start game
     */
    public void startGame () {
        initHand();

        for (int i = 0; i < players.get().size(); i ++) {
            if (players.get().get(i) != null) players.get().get(i).startGame();
        }

        buttons.get().setVoteStart(false);

        frame.get().refresh();
    }

    /**
     * Initialize the hand panel (after server sends down the player's hand)
     */
    public void initHand () {
        References.hand = new HandPanel();
        References.updateReferences();

        hand.get().setVisible(true);

        add(hand.get());
        frame.get().refresh();
    }

    /**
     * Starts the game (when it is player's turn)
     */
    public void startPlay () {
        hand.get().setPlay(true);
        buttons.get().setPlay(true);
        PlayerPanel.getPanel(current_player).setCurrentPlayer(false);

        current_player = Constants.DEFAULT;
    }

    /**
     * Stops the game (when it is no longer player's turn)
     */
    public void stopPlay () {
        buttons.get().setPlay(false);
        hand.get().setPlay(false);
    }

    /**
     * Stops the game temporarily (when user's picking the suit of 8 cards)
     */
    public void temporarilyStopPlay () {
        buttons.get().setPlay(false);
        hand.get().temporarilySetPlay(false);
    }

    /**
     * Update all players
     */
    @ Deprecated
    public void updatePlayers () {

        // because of various errors, all player panels have
        // to be disabled, and re-enabled by the server
        // transmission.
        for (int i = 0; i < players.get().size(); i ++) {
            if (players.get().get(i) != null) {
                players.get().get(i).enable(false);
            }
        }

        player.get().updateList();

        References.refreshWindow();
    }

    /**
     * Update the current player
     * @param num   the number of current player
     */
    public void updateCurrentPlayer (int num) {
        PlayerPanel.getPanel(num).setCurrentPlayer(true);
        if (current_player != Constants.DEFAULT) PlayerPanel.getPanel(current_player).setCurrentPlayer(false);
        current_player = num;

        // update the current player's playerPanel accordingly

        frame.get().refresh();
    }

    /**
     * Updates the current active card
     * @param card  the active card
     */
    public void updateActiveCard (Card card) {
        stack.get().updateActiveCard(card);
        player.get().setActiveCard(card);
        frame.get().refresh();
    }

    /**
     * Reset the player panels (updated version of updatePlayers ())
     * Disable all player panels
     */
    public void resetPlayers () {
        References.updateReferences();

        for (int i = 2; i < players.get().size(); i ++) {
            players.get().get(i).enable(false);
        }
        player.get().updateList();
    }

    /**
     * Refreshes the player panels (updated version of updatePlayers ())
     * (update all player panels based on the opponents hashmap)
     */
    public void refreshPlayers () {
        HashMap<Integer,String> opponents = player.get().getOpponents();

        for (Map.Entry<Integer, String> entry : opponents.entrySet()) {
            Integer player_num = entry.getKey();
            String player_name = entry.getValue();

            PlayerPanel.getPanel(player_num).enable(true, player_name);
        }
    }

    /**
     * Overriden paintComponent method
     * @param g Abstact Graphics Component
     */
    @ Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        // draw background and table

        g2d.drawImage(Resources.background, 0, 0, width, height, null);

        int tableMarginW = (width - Resources.table.getWidth())/2;
        int tableMarginH = (height - Resources.table.getHeight())/2 + 50;

        g2d.drawImage(Resources.table, tableMarginW, tableMarginH,
                Resources.table.getWidth(), Resources.table.getHeight(), null);
    }

    /**
     * Method that updates all weak references
     */
    public static void updateReferences () {
        players = new WeakReference<>(References.players);
        hand = new WeakReference<>(References.hand);
        buttons = new WeakReference<>(References.buttons);
        stack = new WeakReference<>(References.stack);

        frame = new WeakReference<>(References.frame);
        player = new WeakReference<>(References.player);
    }

}
