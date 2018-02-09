package ui;

import common.Console;
import common.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by freddeng on 2018-01-26.
 */
class HostPanel extends JPanel {

    private static final int width = Constants.element("HostPanelW");
    private static final int height = Constants.element("HostPanelH");

    private static final int x = (Constants.element("initWidth") - width) / 2;
    private static final int y = (Constants.element("initHeight") - height) / 2;

    private final static int buttonWidth = Constants.element("ButtonW");
    private final static int buttonHeight = Constants.element("ButtonH");

    private final static int buttonX = (width - buttonWidth) / 2;
    private final static int buttonY = height - buttonHeight;

    TButton play;

    JLabel title;
    JLabel hint;

    JLabel ipAddress;

    protected HostPanel () {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        play = new TButton(buttonX, buttonY, buttonWidth, buttonHeight);

        title = new JLabel("Enter Server Port");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(0, 0, 100, 50);

        hint = new JLabel("In the text field above, enter the port number that will be used.\n" +
                "Other players will need this number to join your game.");
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setBounds(500, 0, 100, 50);

        play.addActionListener(e -> {
            setVisible(false);
        });

        add (play);
        add (title);
        add (hint);

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(Resources.host_button, buttonX, buttonY, buttonWidth, buttonHeight, null);

    }

}
