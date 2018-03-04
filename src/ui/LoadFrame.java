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
 * LoadFrame.java
 *-----------------------------------------------------------------------------
 * This class hosts the LoadPanel when loading.
 *-----------------------------------------------------------------------------
 */

package ui;

import javax.swing.*;
import java.awt.*;

public class LoadFrame extends JFrame {

    private static final int width = 400;   // hard coded size
    private static final int height = 110;

    private static LoadPanel load_panel;    // the loading panel

    public LoadFrame () {

        setLayout(null);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.black);
        setSize(width, height);

        load_panel = new LoadPanel();
        add(load_panel);

        setVisible(true);

    }

    public static LoadPanel requestLoadPanelReference () {
        return load_panel;
    }

}
