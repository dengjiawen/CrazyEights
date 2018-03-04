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
 * StackPanel.java
 *-----------------------------------------------------------------------------
 * This panel is used for displaying the active card.
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import logic.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

class StackPanel extends JPanel {

    private static final int width = Constants.getInt("stackPanelW");   // panel width
    private static final int height = Constants.getInt("stackPanelH");  // panel height

    private static final int x = (Constants.getInt("initWidth") - width) / 2;   // x coord
    private static final int y = Constants.getInt("stackPanelY");               // y coord

    private static final int stack_width = (int)(Constants.getInt("CardWidth") * 1.20f);    // width of the stack
    private static final int stack_height = (int)((float)Resources.card_stack.getHeight() * stack_width/Resources.card_stack.getWidth());   // height of the stack

    private static final int stack_x = Constants.getInt("stackX");  // x position of the stack
    private static final int stack_y = (height - stack_height) / 2;              // y position of the stack

    private static int cardWidth = Constants.getInt("CardWidth");   // card width
    private static int cardHeight = (int)((float)(cardWidth) * Resources.cards[1][1].getHeight()/Resources.cards[1][1].getWidth()); // card height

    private static final int active_x = Constants.getInt("activeX");        // the x position of active card
    private static final int active_y = (height - cardHeight) / 2;                       // the y position of the active card
    private static final int inactive_x = Constants.getInt("inactiveX");   // the x position of the last active card
    private static final int inactive_y = active_y;                                     // the y position of the last active card

    private BufferedImage activeCardRef;        // image of current active card
    private BufferedImage lastActiveCardRef;    // image of last active card

    /**
     * Default Constructor
     */
    protected StackPanel() {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);

        activeCardRef = null;
        lastActiveCardRef = null;

        repaint();

    }

    /**
     * Update the active card
     * @param active_card   the active card
     */
    public void updateActiveCard (Card active_card) {
        lastActiveCardRef = activeCardRef;
        // (set last active card as the current active card
        activeCardRef = Resources.cards[active_card.getSuit()][active_card.getRank()];
    }

    /**
     * Overriden paintComponent class
     * @param g Abstract Graphics Class
     */
    @ Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // draw the current active card at the front
        // draw the last active card at the back, rotated by 15 degrees

        g2d.drawImage(Resources.card_stack, stack_x, stack_y, stack_width, stack_height, null);
        if (lastActiveCardRef != null) {
            AffineTransform inactive_rotation = new AffineTransform();
            inactive_rotation.translate(inactive_x, inactive_y);
            inactive_rotation.rotate(- Math.PI / 12);
            inactive_rotation.scale((double)cardWidth/lastActiveCardRef.getWidth(), (double)cardHeight/lastActiveCardRef.getHeight());
            inactive_rotation.translate(- cardWidth / 2, - cardHeight / 2);
            g2d.drawImage(lastActiveCardRef, inactive_rotation, null);
        }
        if (activeCardRef != null) g2d.drawImage(activeCardRef, active_x, active_y, cardWidth, cardHeight, null);
    }

}
