package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import common.Constants;

class Resources {

    static BufferedImage logo;
    static BufferedImage background;
    static BufferedImage table;

    static BufferedImage highlight;
    static BufferedImage playable;
    static BufferedImage profile;

    static BufferedImage cardPattern;

    static BufferedImage connect_button;
    static BufferedImage host_button;
    static BufferedImage cpu_button;

    static BufferedImage connect_submit;
    static BufferedImage cpu_submit;

    static BufferedImage[][] cards;

    static HashMap<String, BufferedImage> dimmable_Assets;

    static void loadImageAssets () {
        logo = loadImage("core/logo.png");
        background = loadImage("core/background.png");
        table = loadImage("core/table.png");

        highlight = loadImage("cards/highlight.png");
        playable = loadImage("cards/playable.png");

        profile = loadImage("profile.jpg");

        connect_button = loadImage("buttons/connect.png");
        host_button = loadImage("buttons/host.png");
        cpu_button = loadImage("buttons/cpu.png");

        cards = new BufferedImage[4][13];
        for (int i = 0; i < cards.length; i ++) {
            for (int j = 0; j < cards[i].length; j ++) {
                cards[i][j] = loadImage("cards/" + i + "/" + (j + 1) + ".png");
            }
        }

        BufferedImage quit = loadImage("core/quit.png");
        BufferedImage mini = loadImage("core/mini.png");

        dimmable_Assets = new HashMap<String, BufferedImage>() {
            {
                put("quit", quit);
                put("mini", mini);
            }
        };
    }

    static void dimImage (String assetName, boolean doDim) {

        BufferedImage tempRef = dimmable_Assets.get(assetName);

        if (doDim) dimmable_Assets.put(assetName, loadImage("core/" + assetName + "_dim.png"));
        else dimmable_Assets.put(assetName, loadImage("core/" + assetName + ".png"));
    }

    static BufferedImage loadImage (String res_path) {

        try {
            return ImageIO.read(Resources.class.getResource(Constants.UIRes_path + res_path));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}