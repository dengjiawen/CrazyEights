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
 * References.java
 *-----------------------------------------------------------------------------
 * This class holds all the references, and offer connecting and hosting
 * methods.
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import logic.Hand;
import logic.Player;
import logic.Server;
import javax.swing.*;
import java.util.ArrayList;

public class References {

    // the following are all the references of primary JComponents
    public static GameWindow frame;

    public static ShadePanel shade;
    public static GamePanel panel;
    public static TitlePanel title;
    public static MainMenuPanel main_menu;
    public static HostPanel host;
    public static EightPanel eight;
    public static WinnerPanel winner;

    public static Player player;

    public static ArrayList<PlayerPanel> players;
    public static HandPanel hand;
    public static ButtonPanel buttons;
    public static StackPanel stack;

    /**
     * Method that creates a new server
     * @return  integer signalling whether connection was successful
     */
    public static int host() {
        int portNumber = 0;
        String player_name = "";

        // use JOptionPane to gather port number and player name.
        while (true) {
            String input = JOptionPane.showInputDialog(shade, Constants.titleFont + "Enter a Port Number\n" +
                    Constants.bodyFont + "In the text field above, enter the port number that will be used.\n" +
                    Constants.bodyFont + "Other players will need this number to join your game.", "Enter Port Number");
            try {
                if (input != null) {
                    portNumber = Integer.parseInt(input);
                } else {
                    return Constants.ERROR;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(shade, "An invalid port number was entered. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (portNumber <= 25565 && portNumber > 0) {
                try {
                    Server.init(portNumber);
                    player_name = JOptionPane.showInputDialog(null, "Please enter a nickname: ");
                    break;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(shade, "This port cannot be reserved for the game.\n" +
                            "Please try a different port.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        // create a new player using localhost as the server.

        try {
            player = new Player("localhost", portNumber, player_name);
            References.updateReferences();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(shade, "A catastrophic network error had occured.\n" +
                    "Please try again.", "Error", JOptionPane.ERROR_MESSAGE);

            Server.stopListening();

            return Constants.ERROR;
        }

        // update the host panel
        host.updatePort(portNumber);

        return Constants.SUCCESS;
    }

    /**
     * Method that connects to an existing server
     * @return
     */
    public static int connect() {

        int port = 0;

        String name;
        String ip = "";

        // use JOptionPane to ask for IP address, port, and name
        while (true) {
            String input = JOptionPane.showInputDialog("Enter game IP address:");

            try {
                if (input != null) {
                    ip = input;
                    break;
                } else {
                    return Constants.ERROR;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(shade, "An invalid IP address was entered. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (true) {
            String input = JOptionPane.showInputDialog("Enter game port:");
            try {
                if (input != null) {
                    port = Integer.parseInt(input);
                    break;
                } else {
                    return Constants.ERROR;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(shade, "An invalid port was entered. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (true) {
            String input = JOptionPane.showInputDialog("Please enter a nickname: ");

            if (input != null) {
                name = input;
                break;
            } else {
                return Constants.ERROR;
            }
        }

        // initialize a player object
        try {
            player = new Player(ip, port, name);
            updateReferences();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(shade, "A catastrophic network error had occured.\n" +
                    "Please try again.", "Error", JOptionPane.ERROR_MESSAGE);

            return Constants.ERROR;
        }

        return Constants.SUCCESS;
    }

    /**
     * Refresh the whole window
     */
    public static void refreshWindow () {
        frame.refresh();
    }

    /**
     * Update all weak references
     */
    public static void updateReferences () {

        GameWindow.updateReferences();
        GamePanel.updateReferences();

        try {
            HandPanel.updateReferences();
        } catch (NullPointerException e) {

        }

        ButtonPanel.updateReferences();
        EightPanel.updateReferences();
        PlayerPanel.updateReferences();
        Player.updateReferences();
        Hand.updateReferences();
        WinnerPanel.updateReferences();

    }

    /**
     * Reset all references when game session ends
     */
    public static void endGameSession () {

        frame.setVisible(false);
        frame.dispose();
        frame = null;

        panel = null;
        title = null;
        main_menu = null;
        host = null;
        eight = null;
        winner = null;
        player = null;
        players = null;
        hand = null;
        buttons = null;
        stack = null;

        System.gc();

        try {
            frame = new GameWindow();
        } catch (Exception e) {

        }

    }

    /**
     * Handle unexpected connections by ending game session.
     * @param isHost
     */
    public static void unexpectedDisconnect (boolean isHost) {

        player.stopKeepAlive();

        frame.setShade(true);
        panel.stopPlay();

        if (isHost) JOptionPane.showMessageDialog(shade,
                "The host had disconnected from the game. The game cannot continue.", "Error", JOptionPane.ERROR_MESSAGE);
        else JOptionPane.showMessageDialog(shade,
                "A player had disconnected from the game. The game cannot continue.", "Error", JOptionPane.ERROR_MESSAGE);

        endGameSession();
    }
}

