package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

    int curPlayerCount = 1;

    ArrayList<PlayerPanel> player;
    HandPanel hand;

    protected GamePanel () throws Exception {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);
    }

    public void addPlayer (String name, int playerNum) {
        player.add(new PlayerPanel(name, playerNum, ++ curPlayerCount));
        add(player.get(curPlayerCount - 2));
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
