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
 * ShadePanel.java
 *-----------------------------------------------------------------------------
 * This class inherits the JPanel, and is essentially a black shade over the
 * whole window.
 *-----------------------------------------------------------------------------
 */

package ui;

import javax.swing.*;
import java.awt.*;
import common.Constants;

class ShadePanel extends JPanel {

    static int width = Constants.getInt("initWidth");   // frame width
    static int height = Constants.getInt("initHeight"); // frame height

    /**
     * Default Constructor
     */
    protected ShadePanel () {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);
        setBackground(new Color(0,0,0, 200));

    }

    /**
     * If visible, GRAB FOCUS
     * @param isVisible /
     */
    public void setVisible (boolean isVisible) {
        super.setVisible(isVisible);

        if (isVisible) grabFocus();
    }

}
