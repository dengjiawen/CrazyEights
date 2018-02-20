package ui;

import common.Constants;
import logic.Card;
import logic.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by freddeng on 2018-01-26.
 */
class HintPanel extends JPanel {

    private static final int width = 2 * Constants.element("hintW");
    private static final int height = Constants.element("hintH");

    private static final int x = (Constants.element("initWidth") - width) / 2;
    private static final int y = Constants.element("initHeight") - Constants.element("hintYOffset");

    JLabel hint;

    protected HintPanel() {

        super();

        setLayout(null);
        setBounds(x, y, width, height);
        setVisible(false);
        setBackground(new Color(0, 0, 0, 150));

        hint = new JLabel("Welcome to Crazy Eights!");
        hint.setBounds(10, 0, width - 10, height);
        hint.setForeground(Color.white);

        add(hint);
    }

    public void updatePlayerJoin (String name) {
        hint.setText("<html>Player <b>" + name + "</b> had joined the game.</html>");
    }

    public void updateVoteStart (String name, boolean doStart) {
        if (doStart) {
            hint.setText("<html>Player <b>" + name + "</b> had voted to start the game.</html>");
        } else {
            hint.setText("<html>Player <b>" + name + "</b> had withdrawn their vote to start the game.</html>");
        }
    }

    public void startGame () {
        hint.setText("The game had started!");
    }

    public void updateMove (String name, Card card) {

        String card_played = "";
        if (card.getSuit() == Constants.CLUBS) {
            card_played += "♣ ";
        } else if (card.getSuit() == Constants.DIAMONDS) {
            card_played += "♦ ";
        } else if (card.getSuit() == Constants.HEARTS) {
            card_played += "♥ ";
        } else if (card.getSuit() == Constants.SPADES) {
            card_played += "♠ ";
        }

        card_played += Constants.ranks[card.getRank()];

        hint.setText("<html>Player <b>" + name + "</b> had played a <b>" + card_played + "</b>.</html>");
    }

    public void updateEight (String name, Card card) {

        String suit = "";
        if (card.getSuit() == Constants.CLUBS) {
            suit = "♣ CLUBS";
        } else if (card.getSuit() == Constants.DIAMONDS) {
            suit = "♦ DIAMONDS";
        } else if (card.getSuit() == Constants.HEARTS) {
            suit = "♥ HEARTS";
        } else if (card.getSuit() == Constants.SPADES) {
            suit = "♠ SPADES";
        }

        hint.setText("<html>Player <b>" + name + "</b> had played a <b>MAGIC EIGHT!</b> and changed the suit to <b>" +
                suit + "</b>.</html>");
    }

    public void updateSkip (String name) {
        hint.setText("<html>Player <b>" + name + "</b> had skipped their turn.</html>");
    }

    public void goodMove () {
        hint.setText("<html><b>Good move!</b> The next player is now playing.</html>");
    }

    public void skip () {
        hint.setText("<html>You chose to skip your turn.</html>");
    }

    public void pickUp (int pickupNum) {
        hint.setText("<html>You had picked up <b>" + pickupNum + "</b> cards.</html>");
    }

    public void badMove () {
        hint.setText("<html><b>Bad move!</b> You cannot play this card.</html>");
    }

    public void pickUpWarning (int pickupNum) {
        hint.setText("<html>Are you sure that you want to pickup cards? Doing so right now will pickup <b>"
                + pickupNum + "</b> cards.</html>");
    }

    public void yourTurn () {
        hint.setText("It is now your turn to play.");
    }





}
