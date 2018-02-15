package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Console;
import common.Constants;
import common.Misc;
import logic.Hand;
import logic.Sort;

/**
 * Created by freddeng on 2018-01-26.
 */
class HandPanel extends JPanel {

    private static int cardWidth = Constants.element("CardWidth");
    private static int cardHeight = (int)((float)(cardWidth) * Resources.cards[1][1].getHeight()/Resources.cards[1][1].getWidth());

    private static int width = Constants.element("HandPanelWidth");
    private static int height = cardHeight;

    private Hand hand;

    private int x;
    private int y;

    private int individualUsableSpace;
    private int usedSpace;

    MouseAdapter selectionAgent;
    ExecutorService clickHandler;

    private float glowValue = 0f;
    boolean glowIsIncreasing = false;

    Timer glowTimer;

    protected HandPanel (Hand currentHand) {

        super();

        setLayout(null);
        setOpaque(false);

        hand = currentHand;
        Sort.selectionSort(hand);

        clickHandler = Executors.newSingleThreadExecutor();

        glowTimer = new Timer(1000/60, e -> {
            if (glowIsIncreasing) glowValue += 0.1;
            else glowValue -= 0.1;

            if (glowValue > 1) {
                glowValue = 1f;
                glowIsIncreasing = false;
            } else if (glowValue < 0) {
                glowValue = 0f;
                glowIsIncreasing = true;
            }

            repaint();
        });

        selectionAgent = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!hasFocus()) {
                    return;
                }

                int x = e.getX();
                int selectedIndex = 0;

                for (int i = 0; i < hand.size(); i++) {
                    if (Misc.isBetween(i * individualUsableSpace, x, (i + 1) * individualUsableSpace)) {
                        selectedIndex = i;
                        break;
                    } else {
                        selectedIndex = hand.size() - 1;
                    }
                }

                Console.print("" + selectedIndex);

                hand.select(selectedIndex);
                repaint();

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };

        update();

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        update();
        drawCards(g2d);
    }

    private void drawCards (Graphics2D g2d) {

        for (int i = 0; i < hand.size(); i ++) {
            int suit = hand.get(i).getSuit();
            int rank = hand.get(i).getRank();
            g2d.drawImage(Resources.cards[suit][rank - 1], i * individualUsableSpace, 0,
                    cardWidth, cardHeight, null);

            if (hand.isSelected(i)) {
                g2d.drawImage(Resources.highlight, i * individualUsableSpace, 0,
                        cardWidth, cardHeight, null);
            }
            if (hand.isPlayable(i) && !hand.isSelected(i)) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowValue));
                g2d.drawImage(Resources.playable, i * individualUsableSpace, 0, cardWidth, cardHeight, null);
            }
        }
    }

    void update () {

        Sort.selectionSort(hand);

        individualUsableSpace = Constants.element("IndividualSpace");
        usedSpace = cardWidth + individualUsableSpace * hand.size();

        width = usedSpace;

        x = (GamePanel.width - width)/2;
        y = GamePanel.height - height - Constants.element("HandPanelYOffset");

        setBounds(x, y, width, height);
        repaint();
    }

    public void allowToPlay (boolean isAllowed) {
        if (!isAllowed) removeMouseListener(selectionAgent);
        else {
            addMouseListener(selectionAgent);
            glowTimer.start();
        }
    }

}
