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
 * GameWindow.java
 *-----------------------------------------------------------------------------
 * This class inherits JFrame, and represents the main game window.
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import javax.swing.JFrame;
import java.awt.Frame;
import java.lang.ref.WeakReference;

public class GameWindow extends JFrame {

    private static WeakReference <ShadePanel> shade;    // weak reference of window shade
    private static WeakReference <EightPanel> eight;    // weak reference of the panel where user can choose suit of 8 cards

    private static int width = Constants.getInt("initWidth");   // frame width
    private static int height = Constants.getInt("initHeight"); // frame height

    /**
     * Default constructor
     */
    public GameWindow () {

        super();

        setSize(width, height);
        setLayout(null);
        setUndecorated(true);
        setResizable(false);
        setLocationRelativeTo(null);

        // initialize all window elements
        References.frame = this;

        References.panel = new GamePanel();
        References.title = new TitlePanel();
        References.main_menu = new MainMenuPanel();
        References.host = new HostPanel();
        References.eight = new EightPanel();
        References.winner = new WinnerPanel();
        References.shade = new ShadePanel();

        // add all window elements
        add(References.title);
        add(References.host);
        add(References.winner);
        add(References.main_menu);
        add(References.eight);
        add(References.shade);
        add(References.panel);

        // update all weak references
        References.updateReferences();

        // set visible
        setVisible(true);
        setShade(true);

    }

    /**
     * Method that minimizes the window, for the title panel
     */
    void minimize () {
        setState(Frame.ICONIFIED);
    }

    /**
     * Method that controls the window shade (shadePanel)
     * @param isShaded  whether the shade should be activated
     */
    void setShade (boolean isShaded) {
        if (isShaded) shade.get().setVisible(true);
        else shade.get().setVisible(false);

        refresh();
    }

    /**
     * Method that repaints every element on the screen
     */
    void refresh () {
        revalidate();
        repaint();
    }

    /**
     * Method that controls the eightPanel
     * @param isVisible whether the eight panel should be visible
     */
    public void setEightPanel (boolean isVisible) {
        eight.get().setVisible(isVisible);
        setShade(isVisible);
    }

    /**
     * Universal method for updating weak references
     */
    public static void updateReferences () {
        shade = new WeakReference<>(References.shade);
        eight = new WeakReference<>(References.eight);
    }

}
