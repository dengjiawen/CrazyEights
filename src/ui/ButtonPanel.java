package ui;

import common.Console;
import common.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Created by freddeng on 2018-01-26.
 */
class ButtonPanel extends JPanel {

    private static final int width = 2 * Constants.element("ButtonW") + Constants.element("ButtonOffset");
    private static final int height = Constants.element("ButtonH");

    private static final int x = (Constants.element("initWidth") - width) / 2;
    private static final int y = Constants.element("initHeight") - 200;

    private final static int buttonWidth = Constants.element("ButtonW");
    private final static int buttonHeight = Constants.element("ButtonH");
    private final static int buttonOffset = Constants.element("ButtonOffset");

    private final static int centeredButtonX = (width - buttonWidth) / 2;

    int currentStage;

    TButton voteStart;
    TButton unvoteStart;

    protected ButtonPanel () {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        grabFocus();
        currentStage = Constants.VOTE_START;

        voteStart = new TButton(centeredButtonX, 0, buttonWidth, buttonHeight);
        voteStart.setVisible(false);
        voteStart.addActionListener(e -> {
            voteStart.setVisible(false);
            GameWindow.requestRef().player.voteToStart();
            currentStage = Constants.CANCEL_VOTE_START;
            unvoteStart.setVisible(true);
            repaint();
        });

        unvoteStart = new TButton(centeredButtonX, 0, buttonWidth, buttonHeight);
        unvoteStart.setVisible(false);
        unvoteStart.addActionListener(e -> {
            unvoteStart.setVisible(false);
            GameWindow.requestRef().player.unvoteToStart();
            currentStage = Constants.VOTE_START;
            voteStart.setVisible(true);
            repaint();
        });

        add (voteStart);
        add (unvoteStart);

    }

    void startVotingSession () {
        voteStart.setVisible(true);
    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        BufferedImage button1 = null;
        BufferedImage button2 = null;

        int x1 = 0;
        int x2 = 0;

        boolean paint2 = true;

        switch (currentStage) {
            case Constants.VOTE_START:
                button1 = Resources.vote_start_button;
                paint2 = false;
                x1 = centeredButtonX;
                break;
            case Constants.CANCEL_VOTE_START:
                button1 = Resources.cancel_vote_button;
                paint2 = false;
                x1 = centeredButtonX;
                break;
        }

        g2d.drawImage(button1, x1, 0, buttonWidth, buttonHeight, null);
        if (paint2) g2d.drawImage(button2, x2, 0, buttonWidth, buttonHeight, null);

    }

}
