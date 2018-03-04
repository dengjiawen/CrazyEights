package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Console;
import common.Constants;
import common.Misc;
import logic.Hand;
import logic.Player;
import logic.Sort;

/**
 * Created by freddeng on 2018-01-26.
 */
public class HandPanel extends JPanel {

    private static WeakReference<ButtonPanel> buttons;
    private static WeakReference<Player> player;
    private static WeakReference<Hand> hand;

    private static final int card_width = Constants.getInt("CardWidth");
    private static final int card_height = (int)((float)(card_width) *
            Resources.cards[1][1].getHeight()/Resources.cards[1][1].getWidth());

    private static int width = Constants.getInt("HandPanelWidth");
    private static int height = card_height;

    private int x;
    private int y;

    private int individual_usable_space;
    private int total_used_space;

    private MouseAdapter click_handler;
    private ExecutorService click_handler_thread;

    private float glow_value = 0f;
    private boolean glow_value_isIncreasing = false;

    private Timer glow_timer;

    private int selected_index = -1;

    protected HandPanel () {

        super();

        setLayout(null);
        setOpaque(false);

        References.updateReferences();

        Sort.selectionSort(hand.get());

        click_handler_thread = Executors.newSingleThreadExecutor();
        click_handler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();

                for (int i = 0; i < hand.get().size(); i++) {
                    if (Misc.isBetween(i * individual_usable_space, x, (i + 1) * individual_usable_space)) {
                        selected_index = i;
                        break;
                    } else {
                        selected_index = hand.get().size() - 1;
                    }
                }

                hand.get().select(selected_index);
                if (hand.get().getSelectedIndex() != Constants.NO_CARD_SELECTED) {
                    buttons.get().enablePlay(true);
                } else {
                    buttons.get().enablePlay(false);
                }

                repaint();

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                getRootPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };

        glow_timer = new Timer(1000/15, e -> {
            if (glow_value_isIncreasing) glow_value += 0.05;
            else glow_value -= 0.1;

            if (glow_value > 1) {
                glow_value = 1f;
                glow_value_isIncreasing = false;
            } else if (glow_value < 0) {
                glow_value = 0f;
                glow_value_isIncreasing = true;
            }

            repaint();
        });

        updateLayout();

    }

    public void updateLayout () {

        Sort.selectionSort(hand.get());

        individual_usable_space = Constants.getInt("IndividualSpace");
        total_used_space = card_width + individual_usable_space * hand.get().size();

        width = total_used_space;

        x = (Constants.getInt("initWidth") - width)/2;
        y = Constants.getInt("initHeight") - height - Constants.getInt("HandPanelYOffset");

        setBounds(x, y, width, height);
        repaint();

    }

    public void setPlay (boolean doEnable) {

        updateLayout();

        if (!doEnable) {
            removeMouseListener(click_handler);
            glow_timer.stop();
            hand.get().reset();
            repaint();
        }
        else {
            hand.get().findPlayable();

            addMouseListener(click_handler);
            grabFocus();
            glow_timer.start();
        }

        repaint();

    }

    public void temporarilySetPlay (boolean doEnable) {
        updateLayout();

        if (!doEnable) {
            removeMouseListener(click_handler);
            glow_timer.stop();
            repaint();
        } else {
            hand.get().findPlayable();

            addMouseListener(click_handler);
            grabFocus();
            glow_timer.start();
        }

        repaint();
    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        drawCards(g2d);
    }

    private void drawCards (Graphics2D g2d) throws IndexOutOfBoundsException {

        for (int i = 0; i < hand.get().size(); i ++) {
            int suit = hand.get().get(i).getSuit();
            int rank = hand.get().get(i).getRank();

            g2d.drawImage(Resources.cards[suit][rank], i * individual_usable_space, 0,
                    card_width, card_height, null);

            if (i == hand.get().getSelectedIndex()) {
                g2d.drawImage(Resources.highlight, i * individual_usable_space, 0,
                        card_width, card_height, null);
            } else if (hand.get().isPlayable(i)) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glow_value));
                g2d.drawImage(Resources.playable, i * individual_usable_space, 0,
                        card_width, card_height, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }
        }

    }

    public static void updateReferences () throws NullPointerException {
        buttons = new WeakReference<>(References.buttons);
        player = new WeakReference<>(References.player);
        hand = new WeakReference<>(References.player.getHand());
    }

}
