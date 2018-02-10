package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Constants;
import common.Misc;
import logic.Hand;
import logic.Sort;

/**
 * Created by freddeng on 2018-01-26.
 */
class PlayerPanel extends JPanel {

    JLabel nameDisplay;
    String playerName;
    int playerNum;

    protected PlayerPanel (String playerName, int playerNum) {

        super();

        setLayout(null);
        setBounds(200, 200, 300, 75);
        setBackground(new Color(0,0,0, 150));

        this.playerNum = playerNum;
        this.playerName = playerName;

        nameDisplay = new JLabel(playerName);
        nameDisplay.setBounds(90, 0, 200 - 80, 80);
        nameDisplay.setForeground(Color.white);
        nameDisplay.setFont(new Font("Arial", Font.BOLD, 18));

        add(nameDisplay);

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(Resources.profile, 0, 0, 75, 75, null);
    }

}
