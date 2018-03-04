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
 * PlayerPanel.java
 *-----------------------------------------------------------------------------
 * Each opponent gets their own little panel next to their seat.
 *-----------------------------------------------------------------------------
 */

package ui;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import common.Constants;
import logic.Player;

public class PlayerPanel extends JPanel {

    // weak references for player object and all player panels
    private static WeakReference<Player> player;
    private static WeakReference<ArrayList<PlayerPanel>> players;

    // predefined width and height
    private final static int width = Constants.getInt("PlayerPanelW");
    private final static int height = Constants.getInt("PlayerPanelH");

    private JLabel name_display;    // JLabel for displaying name
    private JLabel status1;         // JLabel for displaying number of cards left
    private JLabel status2;         // JLabel for displaying other status

    private String player_name;     // string of the opponent's name

    private int player_number;      // opponent's player number
    private int number_of_cards;    // opponent's number of cards left

    /**
     * Constructor
     * @param sync_player_number        server assigned player number
     * @param assigned_player_number    panel index
     */
    protected PlayerPanel (int sync_player_number, int assigned_player_number) {

        super();

        // initialize variables and JComponents

        this.player_number = assigned_player_number;
        this.number_of_cards = 0;

        int x = Constants.getInt("Player" + player_number + "X");
        int y = Constants.getInt("Player" + player_number + "Y");

        setLayout(null);
        setBackground(new Color(0,0,0, 150));
        setBounds(x, y, width, height);

        name_display = new JLabel();
        name_display.setBounds(90, 10, 200, 20);
        name_display.setForeground(Color.white);
        name_display.setFont(new Font("Arial", Font.BOLD, 18));

        status1 = new JLabel("Waiting for game to start...");
        status1.setBounds(90, 30, 200, 20);
        status1.setForeground(Color.white);
        status1.setFont(new Font("Arial", Font.PLAIN, 15));

        status2 = new JLabel("Waiting for starting vote...");
        status2.setBounds(90, 50, 200, 20);
        status2.setForeground(Color.red);
        status2.setFont(new Font("Arial", Font.PLAIN, 15));

        add(name_display);
        add(status1);
        add(status2);

        References.refreshWindow();

    }

    /**
     * Method for enabling the player panel
     * @param doEnable  whether panel should be enabled
     * @param name  opponent name
     */
    public void enable (boolean doEnable, String name) {
        if (doEnable) {
            player_name = name;
            name_display.setText(player_name);
            setVisible(true);
        } else {
            reset();
            setVisible(false);
        }

        References.refreshWindow();
    }

    /**
     * Method for enabling the player panel, without an opponent name
     * @param doEnable  whether panel should be enabled
     */
    public void enable (boolean doEnable) {
        enable(false, "");
    }

    /**
     * Reset player panel
     */
    public void reset () {
        name_display.setText("");
        status1.setText("Waiting for game to start...");
        status2.setText("Waiting for starting vote...");

        References.refreshWindow();
    }

    /**
     * Method to update opponent's readiness status
     * @param ready whether opponent is ready to start
     */
    public void voteStart (boolean ready) {
        if (ready) {
            this.status2.setForeground(Color.green);
            this.status2.setText("Voted to start");
        } else {
            this.status2.setForeground(Color.red);
            this.status2.setText("Waiting for starting vote...");
        }

        References.refreshWindow();
    }

    /**
     * Method to update the number of cards that the opponent has
     * @param number_of_cards   the number of cards that the opponent has
     */
    public void updateNumCard (int number_of_cards) {
        status1.setText("<html>Player has <b>" + number_of_cards + "</b> cards left.</html>");

        References.refreshWindow();
    }

    /**
     * Method to set the opponent as the current player
     * @param isCurrentPlayer   whether opponent is current player
     */
    public void setCurrentPlayer (boolean isCurrentPlayer) {
        if (isCurrentPlayer) {
            status2.setText("Playing...");
        } else {
            status2.setText(" ");
        }

        References.refreshWindow();
    }

    /**
     * Method to initialize player panels when game starts
     */
    public void startGame () {
        status2.setForeground(Color.white);
        status2.setText("");

        References.refreshWindow();
    }

    /**
     * Method to get the opponent's name
     * @return  opponent's name
     */
    public String getName () {
        return player_name;
    }

    /**
     * Overriden paintComponent method
     * @param g Abstract Graphics Component
     */
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        // draw a placeholder profile image
        g.drawImage(Resources.profile, 0, 0, 75, 75, null);
    }

    /**
     * Method to get the assigned number of opponents
     * @param playerNum the server assigned player number
     * @return  the client assigned player number
     */
    public static int getAssignedNum (int playerNum) {

        int this_player_num = player.get().getNum();
        int assigned_player_num = 0;

        // lots of complex calculations here
        if (playerNum > this_player_num) {
            assigned_player_num = playerNum - this_player_num + 1;
        } else {
            assigned_player_num = 6 - (this_player_num - playerNum) + 1;
        }
        return assigned_player_num;

    }

    /**
     * Method to get a player panel by the server assigned player number
     * @param player_number
     * @return
     */
    public static PlayerPanel getPanel (int player_number) {
        return players.get().get(getAssignedNum(player_number));
    }

    /**
     * Update weak references
     */
    public static void updateReferences () {
        player = new WeakReference<>(References.player);
        players = new WeakReference<>(References.players);
    }

}
