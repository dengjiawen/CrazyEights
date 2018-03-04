/**
 * Copyright 2018 (C) Jiawen Deng. All rights reserved.
 *
 * This document is the property of Jiawen Deng.
 * It is considered confidential and proprietary.
 *
 * This document may not be reproduced or transmitted in any form,
 * in whole or in part, without the express written permission of
 * Jiawen Deng.
 *
 *-----------------------------------------------------------------------------
 * HostPanel.java
 *-----------------------------------------------------------------------------
 * This panel shows the IP and port information when a server is created.
 *-----------------------------------------------------------------------------
 */

package ui;

import common.Constants;
import javax.swing.*;
import java.awt.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;

class HostPanel extends JPanel {

    private static final int width = Constants.getInt("HostPanelW");    // panel width
    private static final int height = Constants.getInt("HostPanelH");   // panel height

    private static final int x = (Constants.getInt("initWidth") - width) / 2;   // panel x pos
    private static final int y = (Constants.getInt("initHeight") - height) / 2; // panel y pos

    private final static int buttonWidth = Constants.getInt("ButtonW");     // button width
    private final static int buttonHeight = Constants.getInt("ButtonH");    // button height

    private final static int buttonX = (width - buttonWidth) / 2;   // button x pos
    private final static int buttonY = height - buttonHeight;       // button y pos

    private static int portNumber = 0;  // port number

    private TButton play;   // button to start playing

    private JLabel title;   // label that shows the IP address
    private JLabel hint;    // label that tells the player what to do
    private JLabel port;    // label that shows the port

    /**
     * Default Constructor
     */
    protected HostPanel () {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        // initialize all daughter JComponents
        play = new TButton(buttonX, buttonY, buttonWidth, buttonHeight);

        try {
            // grab the IP address from Inet4Address.getLocalHost()
            title = new JLabel("<html><div style='text-align: center;'>" +
                    "Your IP: " + Inet4Address.getLocalHost().getHostAddress() + "</div></html>");
        } catch (UnknownHostException e) {
            title = new JLabel("ERROR");
        }
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
            References.frame.setShade(false);
            References.panel.init();
        });

        add (play);
        add (port);
        add (title);
        add (hint);

    }

    /**
     * Update the port
     * @param num   port number
     */
    public void updatePort (int num) {
        setVisible(true);
        portNumber = num;
        port.setText("<html><div style='text-align: center;'>" +
                "Port Number: " + portNumber + "</div></html>");
        play.grabFocus();
    }

    /**
     * Overriden paintComponent Class
     * @param g Abstract Graphics Class
     */
    @ Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // draw the play button graphics
        g2d.drawImage(Resources.cpu_secondary, buttonX, buttonY, buttonWidth, buttonHeight, null);

    }

}
