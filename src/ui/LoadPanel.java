package ui;

import com.sun.awt.AWTUtilities;
import common.Console;
import common.Constants;
import ui.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by freddeng on 2018-02-07.
 */
public class LoadPanel extends JPanel {

    //Constant String used for console output.
    private static final String class_name = "load.LoadPanel";

    private static final int WIDTH = 400;
    private static final int HEIGHT = 110;

    private static final Image load_animation = new ImageIcon(
            Resources.class.getResource(Constants.UIRes_path + "animation/load.gif")).getImage();
    private static final Image logo = new ImageIcon(
            Resources.class.getResource(Constants.UIRes_path + "/core/logo.png")).getImage();

    private static final int LOGO_WIDTH = 200;
    private static final int LOGO_HEIGHT = (int)(logo.getHeight(null) * LOGO_WIDTH / logo.getWidth(null));
    private static final int LOGO_X = (int)((WIDTH - LOGO_WIDTH) / 2f);
    private static final int LOGO_Y = 25;

    private JLabel loadStatus;

    public LoadPanel() {

        setBounds(0, 0, WIDTH, HEIGHT);
        setLayout(null);
        setOpaque(false);

        loadStatus = new JLabel("Loading Assets...");
        loadStatus.setForeground(Color.white);
        loadStatus.setBounds(LOGO_X + 75, 18, 300, 100);

        add(loadStatus);

    }

    public void updateLoadedAsset (String asset_name) {
        loadStatus.setText ("Asset " + asset_name + " loaded.");
    }

    public void updateLoadedConstants (String constant_name) {
        loadStatus.setText ("Constant " + constant_name + " loaded.");
    }

    public void paintComponent (Graphics g) {

        g.drawImage(logo, LOGO_X, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT, null);
        g.drawImage(load_animation, LOGO_X - 15, 20, 100, 100, this);

    }

}
