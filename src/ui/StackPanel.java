package ui;

import common.Constants;
import logic.Hand;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by freddeng on 2018-01-26.
 */
class StackPanel extends JPanel {

    static final int frame_width = Constants.element("initWidth");
    static final int frame_height = Constants.element("initHeight");

    static final int width = Constants.element("stackPanelW");
    static final int height = Constants.element("stackPanelH");

    static final int x = (frame_width - width) / 2;
    static final int y = Constants.element("stackPanelY");

    static final int stack_width = (int)(Constants.element("CardWidth") * 1.20f);
    static final int stack_height = (int)((float)Resources.card_stack.getHeight() * stack_width/Resources.card_stack.getWidth());

    static final int stack_x = Constants.element("stackX");
    static final int stack_y = (height - stack_height) / 2;

    private static int cardWidth = Constants.element("CardWidth");
    private static int cardHeight = (int)((float)(cardWidth) * Resources.cards[1][1].getHeight()/Resources.cards[1][1].getWidth());

    static final int active_x = Constants.element("activeX");
    static final int active_y = (height - cardHeight) / 2;
    static final int inactive_x = Constants.element("inactiveX");
    static final int inactive_y = active_y;

    BufferedImage activeCardRef;
    BufferedImage lastActiveCardRef;

    protected StackPanel() {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);

        activeCardRef = null;
        lastActiveCardRef = null;

        repaint();

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(Resources.card_stack, stack_x, stack_y, stack_width, stack_height, null);
        if (lastActiveCardRef != null) {
            AffineTransform inactive_rotation = new AffineTransform();
            inactive_rotation.translate(inactive_x, inactive_y);
            inactive_rotation.rotate(- Math.PI / 12);
            inactive_rotation.scale((double)cardWidth/lastActiveCardRef.getWidth(), (double)cardHeight/lastActiveCardRef.getHeight());
            inactive_rotation.translate(- cardWidth / 2, - cardHeight / 2);
            g2d.drawImage(lastActiveCardRef, inactive_rotation, null);
        }
        if (activeCardRef != null) g2d.drawImage(activeCardRef, active_x, active_y, cardWidth, cardHeight, null);
    }

}
