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
 * LoadPanel.java
 *-----------------------------------------------------------------------------
 * This class contains the GUI that is displayed when resources are being
 * loaded.
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import javax.swing.*;
import java.awt.*;

public class LoadPanel extends JPanel {

    private static final int WIDTH = 400;   // hard coded width
    private static final int HEIGHT = 110;  // hard coded height

    private static final Image load_animation = new ImageIcon(      // gif of animation
            LoadPanel.class.getResource(Constants.UIRes_path + "animation/load.gif")).getImage();
    private static final Image logo = new ImageIcon(                // png of logo
            LoadPanel.class.getResource(Constants.UIRes_path + "core/logo.png")).getImage();

    // hard coded parameters
    private static final int LOGO_WIDTH = 200;
    private static final int LOGO_HEIGHT = (int)(logo.getHeight(null) * LOGO_WIDTH / logo.getWidth(null));
    private static final int LOGO_X = (int)((WIDTH - LOGO_WIDTH) / 2f);
    private static final int LOGO_Y = 25;

    // JLabel that shows the loading progress
    private JLabel loadStatus;

    /**
     * Default Constructor
     */
    public LoadPanel() {

        setBounds(0, 0, WIDTH, HEIGHT);
        setLayout(null);
        setOpaque(false);

        loadStatus = new JLabel("Loading Assets...");
        loadStatus.setForeground(Color.white);
        loadStatus.setBounds(LOGO_X + 75, 18, 300, 100);

        add(loadStatus);

    }

    /**
     * Updates the asset being loaded
     * @param asset_name    asset name
     */
    public void updateLoadedAsset (String asset_name) {
        loadStatus.setText ("Asset " + asset_name + " loaded.");
    }

    /**
     * Updates the name of the constants being loaded
     * @param constant_name constant name
     */
    @ Deprecated
    public void updateLoadedConstants (String constant_name) {
        loadStatus.setText ("Constant " + constant_name + " loaded.");
    }

    /**
     * Overriden paintComponent method
     * @param g Abstract Graphics Class
     */
    @ Override
    public void paintComponent (Graphics g) {

        g.drawImage(logo, LOGO_X, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT, null);
        g.drawImage(load_animation, LOGO_X - 15, 20, 100, 100, this);

    }

}
