package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import common.Constants;

/**
 * Created by freddeng on 2018-01-26.
 */
class TitlePanel extends JPanel {

    private static int width = Constants.element("titleWidth");
    private static int height = Constants.element("titleHeight");

    private static int logoMarginW = Constants.element("logoMarginW");
    private static int logoMarginH = Constants.element("logoMarginH");
    private static int logoHeight = (height - 2 * logoMarginH);
    private static int logoWidth = (int)(((double)logoHeight/Resources.logo.getHeight()) * Resources.logo.getWidth());

    private static int contMarginH = Constants.element("contMarginH");
    private static int contSL = (height - 2 * contMarginH);
    private static int contMarginW1 = width - 2 * Constants.element("contMarginW") - 2 * contSL;
    private static int contMarginW2 = contMarginW1 + contSL + Constants.element("contMarginW");

    private JButton quit;
    private JButton mini;

    protected TitlePanel () {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);
        setOpaque(false);

        quit = new TButton(contMarginW2, contMarginH, contSL, contSL, "quit");
        mini = new TButton(contMarginW1, contMarginH, contSL, contSL, "mini");

        quit.addActionListener(e -> {System.exit(0);});
        mini.addActionListener(e -> GameWindow.requestRef().minimize());

        add(quit);
        add(mini);

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.drawImage(Resources.logo, logoMarginW, logoMarginH, logoWidth, logoHeight, null);
        g2d.drawImage(Resources.dimmable_Assets.get("quit"), contMarginW2, contMarginH, contSL, contSL, null);
        g2d.drawImage(Resources.dimmable_Assets.get("mini"), contMarginW1, contMarginH, contSL, contSL, null);

    }

}
