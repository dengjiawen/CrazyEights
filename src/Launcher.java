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
 * Launcher.java
 *-----------------------------------------------------------------------------
 * This is the launcher. It contains the main method, loads resources, and
 * initializes the game window.
 *-----------------------------------------------------------------------------
 */

import ui.LoadFrame;
import ui.GameWindow;
import ui.Resources;
import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher {

    private static ExecutorService resource_loader = Executors.newSingleThreadExecutor();   // a seperate thread to import resources
    private static boolean resource_loaded = false;                                         // boolean indicating whether loading is done

    static GameWindow window;   // the main game window
    static LoadFrame load;      // the loading window

    /**
     * Main Class
     */
    public static void main (String[] args) {

        initiateLoading();  // start loading screen
        loadResources();    // start loading resources

        while (!resource_loaded) {
            try {
                Thread.sleep(500);  // wait until loading is done
            } catch (InterruptedException e) {}
        }

        initiateWindow();   // start main game window

    }

    /**
     * Show loading frame
     */
    public static void initiateLoading () {
        SwingUtilities.invokeLater(() -> load = new LoadFrame());   // initialize loading window in Event Dispatch Thread

        try {
            Thread.sleep(3000);     // hang the main thread while frame is loaded
        } catch (InterruptedException e) {}
    }

    /**
     * Show game window
     */
    public static void initiateWindow () {
        SwingUtilities.invokeLater(() -> {
            window = new GameWindow();      // initialize window in Event Dispatch Thread
            load.setVisible(false);         // hide the loading window
            load.dispose();
            load = null;
            System.gc();                    // garbage collection
        });
    }

    /**
     * Load resources
     */
    public static void loadResources () {

        // tell resource class to load resources
        resource_loader.submit(() -> {
            Resources.loadCards();
            Resources.loadCoreAssets();
            Resources.loadButtons();
            Resources.loadMiscAssets();
            resource_loaded = true;
        });
    }

}
