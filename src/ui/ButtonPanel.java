package ui;

import common.Console;
import common.Constants;
import logic.Server;

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
    private static final int y = Constants.element("initHeight") - 250;

    private final static int buttonWidth = Constants.element("ButtonW");
    private final static int buttonHeight = Constants.element("ButtonH");
    private final static int buttonOffset = Constants.element("ButtonOffset");

    private final static int centeredButtonX = (width - buttonWidth) / 2;

    int currentStage;

    TButton voteStart;
    TButton unvoteStart;

    TButton playCard;
    TButton pickUp;
    TButton skipTurn;

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

        playCard = new TButton(0, 0, buttonWidth, buttonHeight);
        playCard.addActionListener(e -> {
            Console.print("pressed");
            GameWindow.player.playCard(GameWindow.player.hand.get(
                    GameWindow.panel.hand.selected
            ));
        });

        pickUp = new TButton(buttonOffset + buttonWidth, 0, buttonWidth, buttonHeight);
        skipTurn = new TButton(buttonOffset + buttonWidth, 0, buttonWidth, buttonHeight);

        playCard.setVisible(false);
        pickUp.setVisible(false);
        skipTurn.setVisible(false);

        add (voteStart);
        add (unvoteStart);

        add (playCard);
        add (pickUp);
        add (skipTurn);

    }

    void startVotingSession () {
        voteStart.setVisible(true);
    }

    void exitVotingSession () {
        voteStart.setVisible(false);
        unvoteStart.setVisible(false);

        currentStage = Constants.DEFAULT;

        GameWindow.requestRef().repaint();
    }

    void startPlayingSession () {
        pickUp.setVisible(true);
        currentStage = Constants.PICKUP_ONLY;

        GameWindow.requestRef().repaint();
    }

    void stopPlayingSession () {
        playCard.setVisible(false);
        pickUp.setVisible(false);
        skipTurn.setVisible(false);

        currentStage = Constants.DEFAULT;

        GameWindow.requestRef().repaint();
    }

    void activatePlayButton (boolean doActivate) {
        if (doActivate) {
            if (currentStage == Constants.PICKUP_ONLY) {
                currentStage = Constants.PLAY_CARD_PICKUP;
                playCard.setVisible(true);
                grabFocus();
                GameWindow.requestRef().repaint();
            } else if (currentStage == Constants.SKIP_TURN_ONLY) {
                currentStage = Constants.PLAY_CARD_SKIP_TURN;
                playCard.setVisible(true);
                grabFocus();
                revalidate();
                repaint();
                GameWindow.requestRef().repaint();
            }
        } else {
            if (currentStage == Constants.PLAY_CARD_PICKUP) {
                currentStage = Constants.PICKUP_ONLY;
                playCard.setVisible(false);
                GameWindow.requestRef().repaint();
            } else if (currentStage == Constants.PLAY_CARD_SKIP_TURN) {
                currentStage = Constants.SKIP_TURN_ONLY;
                playCard.setVisible(false);
                GameWindow.requestRef().repaint();
            }
        }
    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        BufferedImage button1 = null;
        BufferedImage button2 = null;

        int x1 = 0;
        int x2 = buttonWidth + buttonOffset;

        switch (currentStage) {
            case Constants.VOTE_START:
                button1 = Resources.vote_start_button;
                x1 = centeredButtonX;
                break;
            case Constants.CANCEL_VOTE_START:
                button1 = Resources.cancel_vote_button;
                x1 = centeredButtonX;
                break;
            case Constants.PICKUP_ONLY:
                button1 = Resources.play_card_button_inactive;
                button2 = Resources.pickup_button;
                break;
            case Constants.PLAY_CARD_PICKUP:
                button1 = Resources.play_card_button_active;
                button2 = Resources.pickup_button;
                break;
            case Constants.PLAY_CARD_SKIP_TURN:
                button1 = Resources.play_card_button_active;
                button2 = Resources.pickup_button;
                break;
            case Constants.SKIP_TURN_ONLY:
                button1 = Resources.play_card_button_inactive;
                button2 = Resources.pickup_button;
                break;
            default :
                button1 = null;
                button2 = null;
        }

        if (button1 != null) g2d.drawImage(button1, x1, 0, buttonWidth, buttonHeight, null);
        if (button2 != null) g2d.drawImage(button2, x2, 0, buttonWidth, buttonHeight, null);

    }



}
