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
 * WinnerPanel.java
 *-----------------------------------------------------------------------------
 * The panel for displaying the winner of the game.
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.lang.ref.WeakReference;

public class WinnerPanel extends JPanel {

    private static WeakReference<GameWindow> frame;     // weak reference of the game window

    private static final int width = Constants.getInt("HostPanelW");    // panel width
    private static final int height = Constants.getInt("HostPanelH");   // panel height

    private static final int x = (Constants.getInt("initWidth") - width) / 2;   // panel x coord
    private static final int y = (Constants.getInt("initHeight") - height) / 2; // panel y coord

    private final static int buttonWidth = Constants.getInt("ButtonW");     // panel button width
    private final static int buttonHeight = Constants.getInt("ButtonH");    // panel button height

    private final static int buttonX = (width - buttonWidth) / 2;   // panel button x coord
    private final static int buttonY = height - buttonHeight;       // panel button y coord

    private TButton return_to_menu;   // return to main menu button

    private JLabel title;   // shows the title
    private JLabel subtext; // shows the subtext

    /**
     * Default Constructor
     */
    protected WinnerPanel() {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        // initialize buttons and labels
        return_to_menu = new TButton(buttonX, buttonY, buttonWidth, buttonHeight);
        return_to_menu.addActionListener(e -> References.endGameSession());

        title = new JLabel();
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds((width - 300)/2, 100, 300, 100);

        subtext = new JLabel();
        subtext.setForeground(Color.white);
        subtext.setFont(new Font("Arial", Font.BOLD, 12));
        subtext.setBounds((width - 300)/2, 125, 500, 100);

        add (return_to_menu);
        add (subtext);
        add (title);

    }

    /**
     * Method for displaying a "you win" message
     */
    public void youWon () {
        title.setText("<html><div style='text-align: center;'>YOU WON!</div></html>");
        subtext.setText("<html><div style='text-align: center;'>Congratulations, your IQ is higher than the other humans.</div></html>");

        setVisible(true);
        frame.get().setShade(true);
    }

    /**
     * Method for displaying when another player won
     * @param name  name of the winner
     */
    public void otherWon (String name) {
        title.setText("<html><div style='text-align: center;'>Player " + name + " WON!</div></html>");
        subtext.setText("<html><div style='text-align: center;'>You suck. Period.</div></html>");

        setVisible(true);
        frame.get().setShade(true);
    }

    /**
     * Overriden paintComponent method
     * @param g Abstract Graphics Class
     */
    protected void paintComponent (Graphics g) {
        // draw the button graphics
        g.drawImage(Resources.return_button, buttonX, buttonY, buttonWidth, buttonHeight, null);
    }

    /**
     * Method for updating the weak references
     */
    public static void updateReferences () {
        frame = new WeakReference<>(References.frame);
    }

}
