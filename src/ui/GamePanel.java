package ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Constants;
import logic.Hand;
import logic.Player;
import logic.Server;

/**
 * Created by freddeng on 2018-01-26.
 */
class GamePanel extends JPanel {

    static int width = Constants.element("initWidth");
    static int height = Constants.element("initHeight");

    Player player;

    ExecutorService serverThread= Executors.newSingleThreadExecutor();

    protected GamePanel () throws Exception {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);

        Hand hand = new Hand();

        HandPanel handPanel = new HandPanel(hand);
        PlayerPanel player = new PlayerPanel("John", 1);

        add(handPanel);
        add(player);
    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.drawImage(Resources.background, 0, 0, width, height, null);

        int tableMarginW = (width - Resources.table.getWidth())/2;
        int tableMarginH = (height - Resources.table.getHeight())/2 + 50;

        g2d.drawImage(Resources.table, tableMarginW, tableMarginH,
                Resources.table.getWidth(), Resources.table.getHeight(), null);

    }

}
