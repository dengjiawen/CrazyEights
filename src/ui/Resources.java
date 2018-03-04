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
 * Resources.java
 *-----------------------------------------------------------------------------
 * This class contains all of the images etc resources used in the project.
 *-----------------------------------------------------------------------------
 */

package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.HashMap;
import common.Console;
import common.Constants;

public class Resources {

    static BufferedImage logo;
    static BufferedImage background;
    static BufferedImage table;

    static BufferedImage highlight;
    static BufferedImage playable;
    static BufferedImage profile;

    static BufferedImage connect_button;
    static BufferedImage host_button;
    static BufferedImage cpu_secondary;

    static BufferedImage card_stack;

    static BufferedImage vote_start_button;
    static BufferedImage cancel_vote_button;

    static BufferedImage play_card_button_active;
    static BufferedImage play_card_button_inactive;
    static BufferedImage pickup_button;
    static BufferedImage skipturn_button;
    static BufferedImage cancel_button;
    static BufferedImage return_button;

    static BufferedImage[][] cards;

    static HashMap<String, BufferedImage> dimmable_Assets;

    /**
     * Method to load assets
     */
    public static void loadMiscAssets () {

        profile = loadImage("profile.jpg", true);
        card_stack = loadImage("cards/stack.png", true);
    }

    public static void loadButtons () {
        connect_button = loadImage("buttons/connect.png", true);
        host_button = loadImage("buttons/host.png", true);
        cpu_secondary = loadImage("buttons/cpu_secondary.png", true);

        vote_start_button = loadImage("buttons/vote_start.png", true);
        cancel_vote_button = loadImage("buttons/cancel_vote.png", true);

        play_card_button_active = loadImage("buttons/play_card.png", true);
        play_card_button_inactive = convertToGrayScale(play_card_button_active);
        pickup_button = loadImage("buttons/pickup.png", true);
        skipturn_button = loadImage("buttons/skip_turn.png", true);

        cancel_button = loadImage("buttons/cancel.png", true);
        return_button = loadImage("buttons/return.png", true);

    }

    /**
     * Method to load assets
     */
    public static void loadCoreAssets () {
        logo = loadImage("core/logo.png", true);
        background = loadImage("core/background.png", true);
        table = loadImage("core/table.png", true);

        BufferedImage quit = loadImage("core/quit.png", true);
        BufferedImage mini = loadImage("core/mini.png", true);

        dimmable_Assets = new HashMap<String, BufferedImage>() {
            {
                put("quit", quit);
                put("mini", mini);
            }
        };
    }

    /**
     * Method to load assets
     */
    public static void loadCards () {
        cards = new BufferedImage[4][13 + 1];
        for (int i = 0; i < cards.length; i ++) {
            cards[i][0] = null;
            for (int j = 1; j < cards[i].length; j ++) {
                cards[i][j] = loadImage("cards/" + i + "/" + j + ".png", true);
            }
        }

        highlight = loadImage("cards/highlight.png", true);
        playable = loadImage("cards/playable.png", true);
    }

    /**
     * Special method to change a button's state when mouse hovers over it.
     * @param assetName the name of the asset to be dimmed
     * @param doDim     whether asset should be dimmed
     */
    public static void dimImage (String assetName, boolean doDim) {

        BufferedImage tempRef = dimmable_Assets.get(assetName);

        // bufferedimages are replaced with darkened version if necessary
        if (doDim) dimmable_Assets.put(assetName, loadImage("core/" + assetName + "_dim.png"));
        else dimmable_Assets.put(assetName, loadImage("core/" + assetName + ".png"));
    }

    /**
     * Method that loads an image, and broadcasts it on the loading panel
     * @param res_path      path to the resource
     * @param doBroadcast   whether it should be broadcasted
     * @return  BufferedImage
     */
    private static BufferedImage loadImage (String res_path, boolean doBroadcast) {
        if (doBroadcast) SwingUtilities.invokeLater(() -> LoadFrame.requestLoadPanelReference().updateLoadedAsset(res_path));
        return loadImage(res_path);
    }

    /**
     * Method that loads an image using ImageIO.
     * @param res_path  path to resource
     * @return  BufferedImage
     */
    private static BufferedImage loadImage (String res_path) {

        try {
            Console.printGeneralMessage("Found resource [" + res_path + "]", Resources.class.getName());
            return ImageIO.read(Resources.class.getResource(Constants.UIRes_path + res_path));
        } catch (Exception e) {
            Console.printErrorMessage("[Exception]" + e.getMessage(), Resources.class.getName());
            JOptionPane.showMessageDialog(null, "Resources are missing.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(100);
        }

        return null;

    }

    /**
     * Converts a bufferedimage to grey scale
     * @param image image to be converted
     * @return  converted image
     */
    private static BufferedImage convertToGrayScale(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(image, result);
        return result;
    }

}