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
    ButtonPanel buttons;

    protected GamePanel () throws Exception {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);

        buttons = new ButtonPanel();

        player = new ArrayList<>();
        player.add(null);
        player.add(null);
        for (int i = 2; i <= Constants.element("MaxPlayer"); i ++) {
            player.add(new PlayerPanel("PLACEHOLDER", 0, i));
            player.get(i).setVisible(false);
            add(player.get(i));
        }

        add (buttons);
    }

    public void addPlayer (String name, int playerNum) {

        int assigned_player_num = getAssignedNum(playerNum);

        curPlayerCount ++;
        Console.print(""+ assigned_player_num);

        player.get(assigned_player_num).updateInfo(name);
        player.get(assigned_player_num).setVisible(true);

        GameWindow.requestRef().repaint();
        GameWindow.requestRef().revalidate();
    }

    public int getAssignedNum (int playerNum) {
        int this_player_num = GameWindow.requestRef().player.playerNum;
        int assigned_player_num = 0;

        if (playerNum > this_player_num) {
            assigned_player_num = playerNum - this_player_num + 1;
        } else {
            assigned_player_num = 6 - (this_player_num - playerNum) + 1;
        }
        return assigned_player_num;
    }

    public void removePlayer () {
        for (int i = 0; i < player.size(); i ++) {
            if (player.get(i) != null) {
                player.get(i).setVisible(false);
                player.get(i).reset();
            }
        }

        curPlayerCount = 1;

        GameWindow.requestRef().revalidate();
        GameWindow.requestRef().repaint();
        GameWindow.requestRef().player.updateList();
    }

    public void updateReadyStatus (int playerNum, boolean ready) {
        player.get(getAssignedNum(playerNum)).voteReady(ready);
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

    public void startVotingSession () {
        buttons.setVisible(true);
        buttons.startVotingSession();
    }

}
