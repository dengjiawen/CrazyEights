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
 * TitlePanel.java
 *-----------------------------------------------------------------------------
 * This panel is used for displaying the logo and exit/minimize controls.
 *-----------------------------------------------------------------------------
 */

package ui;

import javax.swing.*;
import java.awt.*;

import common.Constants;

class TitlePanel extends JPanel {

    private static int width = Constants.getInt("titleWidth");      // panel width
    private static int height = Constants.getInt("titleHeight");    // panel height

    private static int logoMarginW = Constants.getInt("logoMarginW");   // logo margins
    private static int logoMarginH = Constants.getInt("logoMarginH");
    private static int logoHeight = (height - 2 * logoMarginH);           // logo dimensions
    private static int logoWidth = (int)(((double)logoHeight/Resources.logo.getHeight()) * Resources.logo.getWidth());

    private static int contMarginH = Constants.getInt("contMarginH");   // control margins
    private static int contSL = (height - 2 * contMarginH);          // control side lengths
    private static int contMarginW1 = width - 2 * Constants.getInt("contMarginW") - 2 * contSL; // control margins
    private static int contMarginW2 = contMarginW1 + contSL + Constants.getInt("contMarginW");  // control margins

    private TButton quit;   // control (quit)
    private TButton mini;   // control (minimize)

    /**
     * Default Constructor
     */
    protected TitlePanel () {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);
        setOpaque(false);

        // initialize buttons
        quit = new TButton(contMarginW2, contMarginH, contSL, contSL, "quit");
        mini = new TButton(contMarginW1, contMarginH, contSL, contSL, "mini");

        quit.addActionListener(e -> System.exit(0));
        mini.addActionListener(e -> References.frame.minimize());

        add(quit);
        add(mini);

    }

    /**
     * Overriden paintComponent method
     * @param g Abstract Graphics Class
     */
    @ Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        // draw the logo and exit/minimize control buttons
        g2d.drawImage(Resources.logo, logoMarginW, logoMarginH, logoWidth, logoHeight, null);
        g2d.drawImage(Resources.dimmable_Assets.get("quit"), contMarginW2, contMarginH, contSL, contSL, null);
        g2d.drawImage(Resources.dimmable_Assets.get("mini"), contMarginW1, contMarginH, contSL, contSL, null);

    }

}
