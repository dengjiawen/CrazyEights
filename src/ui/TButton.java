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
 * TButton.java
 *-----------------------------------------------------------------------------
 * A variation of JButton that is transparent.
 *-----------------------------------------------------------------------------
 */

package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TButton extends JButton {

    // a string that records the name of the
    // dimmable asset; for buttons with alternate
    // state when mouse hovers over it only.
    private String dimmable_asset;

    /**
     * Default Constructor
     */
    protected TButton () {

        super();

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

    }

    /**
     * Constructor without dimmable asset
     * @param x     x position
     * @param y     y position
     * @param width     width
     * @param height    height
     */
    protected TButton (int x, int y, int width, int height) {

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setBounds(x, y, width, height);

        // mouselistener that changes the cursor when hovering
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                getRootPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    /**
     * Constructor with dimmable asset
     * @param x     x position
     * @param y     y position
     * @param width     width
     * @param height    height
     * @param dimmable_asset_name   name of the dimmable assets
     */
    protected TButton (int x, int y, int width, int height, String dimmable_asset_name) {

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setBounds(x, y, width, height);

        dimmable_asset = dimmable_asset_name;

        // mouse listener that change cursor when hovering, AND
        // change button state when hovering (with dimmable assset)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Resources.dimImage(dimmable_asset, true);
                getRootPane().repaint();

                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Resources.dimImage(dimmable_asset, false);
                getRootPane().repaint();

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

    }
}
