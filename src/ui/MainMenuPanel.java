package ui;

import common.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by freddeng on 2018-01-26.
 */
class MainMenuPanel extends JPanel {

    private final static int buttonWidth = Constants.element("ButtonW");
    private final static int buttonHeight = Constants.element("ButtonH");
    private final static int buttonOffset = Constants.element("ButtonOffset");

    private static final int width = buttonWidth;
    private static final int height = 3 * buttonHeight + 2 * buttonOffset;

    private final static int x = (Constants.element("initWidth") - width) / 2;
    private final static int y = (Constants.element("initHeight") - width) / 2;

    TButton connect;
    TButton host;
    TButton cpu;

    protected MainMenuPanel () {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);

        host = new TButton(0, 0, buttonWidth, buttonHeight);
        connect = new TButton(0, buttonHeight + buttonOffset, buttonWidth, buttonHeight);
        cpu = new TButton(0, 2 * buttonHeight + 2 * buttonOffset, buttonWidth, buttonHeight);

        host.addActionListener(e -> {
            setVisible(false);
            int statusCode = GameWindow.requestRef().host();

            if (statusCode == Constants.ERROR) setVisible(true);
        });

        connect.addActionListener(e -> {
            setVisible(false);
            int statusCode = GameWindow.requestRef().connect();

            if (statusCode == Constants.ERROR) setVisible(true);
            else {
                GameWindow.requestRef().setShade(false);
                GameWindow.requestRef().startVotingSession();
            }
        });

        cpu.addActionListener(e -> {
            setVisible(false);
        });

        add (host);
        add (connect);
        add (cpu);

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(Resources.host_button, 0, 0, buttonWidth, buttonHeight, null);
        g2d.drawImage(Resources.connect_button, 0, buttonHeight + buttonOffset, buttonWidth, buttonHeight, null);
        g2d.drawImage(Resources.cpu_button, 0, 2 * buttonHeight + 2 * buttonOffset, buttonWidth, buttonHeight, null);

    }

}
