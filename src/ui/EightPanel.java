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
 * EightPanel.java
 *-----------------------------------------------------------------------------
 * This panel allows player to choose a suit for their 8 card.
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import logic.Player;
import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;

class EightPanel extends JPanel {

    private static WeakReference<GameWindow> frame;     // weak reference for game window
    private static WeakReference<GamePanel> panel;      // weak reference for game panel
    private static WeakReference<Player> player;        // weak reference for player object

    private static final int card_width = Constants.getInt("CardWidth");        // card width
    private static final int card_height = Resources.cards[1][1].getHeight() * card_width    // card height
            / Resources.cards[1][1].getWidth();
    private static final int card_offset = Constants.getInt("ButtonOffset");   // space between cards

    private static final int width = 4 * card_width + 3 * Constants.getInt("ButtonOffset"); // panel width
    private static final int height = card_height * 2;                                                   // panel height

    private static final int x = (Constants.getInt("initWidth") - width) / 2;   // x coord
    private static final int y = height - 10;                                                // y coord

    private final static int buttonWidth = Constants.getInt("ButtonW");         // button width
    private final static int buttonHeight = Constants.getInt("ButtonH");        // button height

    private final static int buttonX = (width - buttonWidth) / 2;       // button x coord
    private final static int buttonY = height - buttonHeight;           // button y coord

    private TButton[] suit_selections;      // array of buttons for suit selection
    private TButton cancel;                 // cancel button

    /**
     * Default Constructor
     */
    protected EightPanel() {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        References.updateReferences();

        // initialize button for choosing suit
        // when pressed, plays an 8 card of the chosen suit
        suit_selections = new TButton[4];
        for (int i = 0; i < suit_selections.length; i ++) {
            final int suit = i;
            suit_selections[i] = new TButton(i * card_width + i * card_offset, 0, card_width, card_height);
            suit_selections[i].addActionListener(e -> {
                player.get().playEight(suit);
                frame.get().setEightPanel(false);
            });
            add(suit_selections[i]);
        }

        // cancel button to return to handpanel
        cancel = new TButton(buttonX, buttonY, buttonWidth, buttonHeight);
        cancel.addActionListener(e -> {
            frame.get().setEightPanel(false);
            panel.get().startPlay();
        });

        add(cancel);

    }

    /**
     * Overriden setVisible method
     * (if visible, stop play temporarily)
     * @param isVisible whether panel should be visible
     */
    @ Override
    public void setVisible (boolean isVisible) {
        super.setVisible(isVisible);

        if (isVisible) grabFocus();

        if (isVisible) {
            try {
                panel.get().temporarilyStopPlay();
            } catch (NullPointerException e) {}
        }
    }

    /**
     * Overriden paintComponent method
     * @param g Abstract Graphics Class
     */
    @ Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        // draw the 8 card in four suits
        for (int i = 0; i < suit_selections.length; i++) {
            g.drawImage(Resources.cards[i][8], i * card_width + i * card_offset, 0, card_width, card_height, null);
        }

        // draw the cancel button
        g.drawImage(Resources.cancel_button, buttonX, buttonY, buttonWidth, buttonHeight, null);

    }

    /**
     * Update all weak references
     */
    public static void updateReferences () {
        frame = new WeakReference<>(References.frame);
        panel = new WeakReference<>(References.panel);
        player = new WeakReference<>(References.player);
    }

}
