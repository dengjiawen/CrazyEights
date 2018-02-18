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

    private final static int width = Constants.element("PlayerPanelW");
    private final static int height = Constants.element("PlayerPanelH");

    JLabel nameDisplay;
    JLabel status;
    JLabel vote;

    String playerName;

    int playerNum;
    int numCards;

    protected PlayerPanel (String playerName, int sync_playerNum, int assigned_playerNum) {

        super();

        this.playerNum = assigned_playerNum;
        this.playerName = playerName;
        this.numCards = 0;

        int x = Constants.element("Player" + playerNum + "X");
        int y = Constants.element("Player" + playerNum + "Y");

        setLayout(null);
        setBackground(new Color(0,0,0, 150));
        setBounds(x, y, width, height);

        nameDisplay = new JLabel(playerName);
        nameDisplay.setBounds(90, 10, 200, 20);
        nameDisplay.setForeground(Color.white);
        nameDisplay.setFont(new Font("Arial", Font.BOLD, 18));

        status = new JLabel("Waiting for game to start...");
        status.setBounds(90, 30, 200, 20);
        status.setForeground(Color.white);
        status.setFont(new Font("Arial", Font.PLAIN, 15));

        vote = new JLabel("Waiting for starting vote...");
        vote.setBounds(90, 50, 200, 20);
        vote.setForeground(Color.red);
        vote.setFont(new Font("Arial", Font.PLAIN, 15));

        add(nameDisplay);
        add(vote);
        add(status);

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(Resources.profile, 0, 0, 75, 75, null);
    }

    public void updateInfo (String name) {
        this.playerName = name;
        nameDisplay.setText(playerName);
        repaint();
    }

    public void exitVoteStatus () {
        vote.setVisible(false);
        vote.setForeground(Color.white);
        status.setText("Waiting for response...");
        revalidate();
        repaint();
    }

    public void reset () {
        voteReady(false);
        setVisible(false);
    }

    public void voteReady (boolean ready) {
        if (ready) {
            this.vote.setForeground(Color.green);
            this.vote.setText("Voted to start");
            setVisible(false);
            repaint();
            setVisible(true);
            repaint();
            GameWindow.requestRef().revalidate();
            GameWindow.requestRef().repaint();
        } else {
            this.vote.setForeground(Color.red);
            this.vote.setText("Waiting for starting vote...");
            setVisible(false);
            repaint();
            setVisible(true);
            repaint();
            GameWindow.requestRef().revalidate();
            GameWindow.requestRef().repaint();
        }
    }

    public void updateNumCard (int numCards) {
        status.setText("<html>Player has <b>" + numCards + "</b> cards left.</html>");
        revalidate();
        repaint();
    }

    public void setCurrentPlayer (boolean isCurrentPlayer) {
        if (isCurrentPlayer) {
            vote.setVisible(true);
            vote.setText("Playing...");
            repaint();
        } else {
            vote.setVisible(false);
            repaint();
        }
    }

}
