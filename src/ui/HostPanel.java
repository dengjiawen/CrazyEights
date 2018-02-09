package ui;

import common.Console;
import common.Constants;

import javax.swing.*;
import java.awt.*;
import java.net.Inet4Address;
import java.net.InetAddress;

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

    private static int portNumber = 0;

    TButton play;

    JLabel title;
    JLabel hint;
    JLabel port;

    protected HostPanel () throws Exception {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        play = new TButton(buttonX, buttonY, buttonWidth, buttonHeight);

        title = new JLabel("<html><div style='text-align: center;'>" +
                "Your IP: " + Inet4Address.getLocalHost().getHostAddress() + "</div></html>");
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds((width - 300)/2, 100, 300, 100);

        port = new JLabel("<html><div style='text-align: center;'>" +
                "Port Number: " + portNumber + "</div></html>");
        port.setForeground(Color.white);
        port.setFont(new Font("Arial", Font.BOLD, 18));
        port.setBounds((width - 300)/2, 125, 300, 100);

        hint = new JLabel("<html><div style='text-align: center;'>" +
                "Enter the above IP address and port number to join this game." + "</div></html>");
        hint.setForeground(Color.white);
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setBounds((width - 300)/2, 180, 500, 50);

        play.addActionListener(e -> {
            setVisible(false);
        });

        add (play);
        add (port);
        add (title);
        add (hint);

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(Resources.host_button, buttonX, buttonY, buttonWidth, buttonHeight, null);

    }

    public void updatePort (int num) {
        setVisible(true);
        portNumber = num;
        port.setText("<html><div style='text-align: center;'>" +
                "Port Number: " + portNumber + "</div></html>");
        repaint();
    }

}
