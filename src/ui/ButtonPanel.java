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
 * ButtonPanel.java
 *-----------------------------------------------------------------------------
 * This panel is used for hosting all the buttons (incl. voting and play/skip)
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import logic.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

class ButtonPanel extends JPanel {

    private static WeakReference<Player> player;    // weak reference of the player object

    private static final int width = 2 * Constants.getInt("ButtonW") +     // panel width
            Constants.getInt("ButtonOffset");
    private static final int height = Constants.getInt("ButtonH");         // panel height

    private static final int button_width = Constants.getInt("ButtonW");        // button width
    private static final int button_height = Constants.getInt("ButtonH");       // button height
    private static final int button_offset = Constants.getInt("ButtonOffset");  // spacing between buttons

    private static final int x = (Constants.getInt("initWidth") - width) / 2;   // panel x coord
    private static final int y = Constants.getInt("initHeight") - 250;          // panel y coord

    private final static int centered_button_x = (width - button_width) / 2;    // x coord for centered button

    private int current_mode;       // current state of the panel

    private TButton vote_start;     // button to vote start
    private TButton unvote_start;   // button to unvote start

    private TButton play_card;      // button to play card
    private TButton pickup;         // button to pick up card
    private TButton skip;           // button to skip turn

    /**
     * Default Constructor
     */
    protected ButtonPanel () {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);
        grabFocus();

        References.updateReferences();

        // if vote start button is pressed, vote start and set button state to
        // unvote start
        vote_start = new TButton(centered_button_x, 0, button_width, button_height);
        vote_start.setVisible(false);
        vote_start.addActionListener(e -> {
            player.get().voteToStart();
            setMode(Constants.CANCEL_VOTE_START);
        });

        // if unvote start button is pressed, unvote start and set button state to
        // vote start
        unvote_start = new TButton(centered_button_x, 0, button_width, button_height);
        unvote_start.setVisible(false);
        unvote_start.addActionListener(e -> {
            player.get().unvoteToStart();
            setMode(Constants.VOTE_START);
        });

        // if play card is pressed, play selected card
        play_card = new TButton(0, 0, button_width, button_height);
        play_card.addActionListener(e -> player.get().playCard());

        // if pickup is pressed, pick up cards and change state
        // to skip turn
        pickup = new TButton(button_offset + button_width, 0, button_width, button_height);
        pickup.addActionListener(e -> {
            player.get().pickUp();
            setMode(Constants.SKIP_TURN_ONLY);
        });

        // if skip turn is pressed, skip turn.
        skip = new TButton(button_offset + button_width, 0, button_width, button_height);
        skip.addActionListener(e -> {
            player.get().skip();
            References.panel.stopPlay();
        });

        add (vote_start);
        add (unvote_start);

        add (play_card);
        add (pickup);
        add (skip);

        // set state to default (no buttons)
        setMode(Constants.DEFAULT);

    }

    /**
     * Start voting process
     * @param doStart   whether voting process should be started
     */
    void setVoteStart (boolean doStart) {
        if (doStart) setMode(Constants.VOTE_START);
        else setMode(Constants.DEFAULT);
    }

    /**
     * Start rematch voting process (planned but unused)
     * @param doStart   whether rematch process should be started
     */
    void setVoteRematch (boolean doStart) {
        if (doStart) setMode(Constants.VOTE_REMATCH_GGNORE);
        else setMode(Constants.DEFAULT);
    }

    /**
     * Start player's turn
     * @param doStart   whether the player's turn should be started
     */
    void setPlay (boolean doStart) {
        if (doStart) setMode(Constants.PICKUP_ONLY);
        else setMode(Constants.DEFAULT);
    }

    /**
     * Enable the play button when the user
     * had selected a card.
     * @param doEnable  whether the play button should be enabled
     */
    void enablePlay (boolean doEnable) {
        if (doEnable) {
            if (current_mode == Constants.PICKUP_ONLY) {
                setMode(Constants.PLAY_CARD_PICKUP);
            } else if (current_mode == Constants.SKIP_TURN_ONLY) {
                setMode(Constants.PLAY_CARD_SKIP_TURN);
            }
        } else {
            if (current_mode == Constants.PLAY_CARD_PICKUP) {
                setMode(Constants.PICKUP_ONLY);
            } else if (current_mode == Constants.PLAY_CARD_SKIP_TURN) {
                setMode(Constants.SKIP_TURN_ONLY);
            }
        }
    }

    /**
     * Method that sets the mode of the panel
     * @param mode_type the target mode type
     */
    void setMode (byte mode_type) {

        current_mode = mode_type;

        // enable and disable button depending on the mode chosen

        switch (mode_type) {
            case Constants.VOTE_START:
                vote_start.setVisible(true);
                unvote_start.setVisible(false);
                break;
            case Constants.CANCEL_VOTE_START:
                vote_start.setVisible(false);
                unvote_start.setVisible(true);
                break;
            case Constants.PICKUP_ONLY:
                play_card.setVisible(false);
                pickup.setVisible(true);
                break;
            case Constants.PLAY_CARD_PICKUP:
                play_card.setVisible(true);
                pickup.setVisible(true);
                break;
            case Constants.PLAY_CARD_SKIP_TURN:
                pickup.setVisible(false);
                play_card.setVisible(true);
                skip.setVisible(true);
                break;
            case Constants.SKIP_TURN_ONLY:
                pickup.setVisible(false);
                play_card.setVisible(false);
                skip.setVisible(true);
                break;
            default:
                vote_start.setVisible(false);
                unvote_start.setVisible(false);
                play_card.setVisible(false);
                pickup.setVisible(false);
                skip.setVisible(false);
        }

        if (mode_type != Constants.DEFAULT) grabFocus();

        References.refreshWindow();
    }

    /**
     * Overridden paintComponent class
     * @param g Abstract Graphics Class
     */
    @ Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        BufferedImage button1 = null;
        BufferedImage button2 = null;

        int x1 = 0;
        int x2 = button_width + button_offset;

        // set button graphics depending on the mode

        switch (current_mode) {
            case Constants.VOTE_START:
                button1 = Resources.vote_start_button;
                x1 = centered_button_x;
                break;
            case Constants.CANCEL_VOTE_START:
                button1 = Resources.cancel_vote_button;
                x1 = centered_button_x;
                break;
            case Constants.PICKUP_ONLY:
                button1 = Resources.play_card_button_inactive;
                button2 = Resources.pickup_button;
                break;
            case Constants.PLAY_CARD_PICKUP:
                button1 = Resources.play_card_button_active;
                button2 = Resources.pickup_button;
                break;
            case Constants.PLAY_CARD_SKIP_TURN:
                button1 = Resources.play_card_button_active;
                button2 = Resources.skipturn_button;
                break;
            case Constants.SKIP_TURN_ONLY:
                button1 = Resources.play_card_button_inactive;
                button2 = Resources.skipturn_button;
                break;
            default :
                button1 = null;
                button2 = null;
        }

        // draw button graphics
        if (button1 != null) g2d.drawImage(button1, x1, 0, button_width, button_height, null);
        if (button2 != null) g2d.drawImage(button2, x2, 0, button_width, button_height, null);

    }

    /**
     * Update all weak references
     */
    public static void updateReferences () {
        player = new WeakReference<>(References.player);
    }

}
