package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Console;
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

        player = new ArrayList<>();
    }

    public void addPlayer (String name, int playerNum) {
        int this_player_num = GameWindow.requestRef().player.playerNum;
        int assigned_player_num = 0;

        if (playerNum > this_player_num) {
            assigned_player_num = playerNum - this_player_num + 1;
        } else {
            assigned_player_num = 6 - (this_player_num - playerNum) + 1;
        }

        curPlayerCount ++;
        Console.print(""+ assigned_player_num);

        player.add(new PlayerPanel(name, playerNum, assigned_player_num));
        add(player.get(curPlayerCount - 2));

        GameWindow.requestRef().repaint();
        GameWindow.requestRef().revalidate();
    }

    public void removePlayer () {
        for (int i = 0; i < player.size(); i ++) {
            player.get(i).invalidate();
            player.get(i).setVisible(false);
            player.set(i, null);
            player.remove(i);
        }

        removeAll();

        curPlayerCount = 1;

        GameWindow.requestRef().repaint();
        GameWindow.requestRef().revalidate();
        GameWindow.requestRef().player.updateList();
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
