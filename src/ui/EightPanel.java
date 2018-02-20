package ui;

import common.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by freddeng on 2018-01-26.
 */
class EightPanel extends JPanel {

    private static final int card_width = Constants.getInt("CardWidth");
    private static final int card_height = Resources.cards[1][1].getHeight() * card_width / Resources.cards[1][1].getWidth();
    private static final int card_offset = Constants.getInt("ButtonOffset");

    private static final int width = 4 * card_width + 3 * Constants.getInt("ButtonOffset");
    private static final int height = card_height;

    private static final int x = (Constants.getInt("initWidth") - width) / 2;
    private static final int y = (Constants.getInt("initHeight") - height) / 2;

    TButton[] suit_selections;

    protected EightPanel() {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        suit_selections = new TButton[4];
        for (int i = 0; i < suit_selections.length; i ++) {
            final int suit = i;
            suit_selections[i] = new TButton(i * card_width + i * card_offset, 0, card_width, card_height);
            suit_selections[i].addActionListener(e -> {
                informRank(suit);
                GameWindow.requestRef().setEightPanel(false);
            });
            add(suit_selections[i]);
        }

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < suit_selections.length; i++) {
            g.drawImage(Resources.cards[i][8], i * card_width + i * card_offset, 0, card_width, card_height, null);
        }

    }

    public void setVisible (boolean isVisible) {
        super.setVisible(isVisible);

        if (isVisible) grabFocus();

        try {
            GameWindow.requestRef().nextPlayer();
        } catch (NullPointerException error) {
            System.out.println("FUCK YOU");
        }
    }


    public void informRank (int suit) {
        GameWindow.player.playEight(GameWindow.player.hand.get(
                GameWindow.panel.hand.selected), suit);
    }



}
