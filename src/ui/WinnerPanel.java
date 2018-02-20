package ui;

import common.Constants;

import javax.swing.*;
import java.awt.*;
import java.net.Inet4Address;

/**
 * Created by freddeng on 2018-01-26.
 */
class WinnerPanel extends JPanel {

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
    JLabel port;

    protected WinnerPanel() throws Exception {

        super();

        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);
        setVisible(false);

        play = new TButton(buttonX, buttonY, buttonWidth, buttonHeight);

        title = new JLabel();
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds((width - 300)/2, 100, 300, 100);

        port = new JLabel();
        port.setForeground(Color.white);
        port.setFont(new Font("Arial", Font.BOLD, 12));
        port.setBounds((width - 300)/2, 125, 500, 100);

        hint = new JLabel();
        hint.setForeground(Color.white);
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setBounds((width - 300)/2, 180, 500, 50);

        play.addActionListener(e -> {
            setVisible(false);
            GameWindow.requestRef().setShade(false);
            GameWindow.requestRef().startVotingSession();
        });

        add (play);
        add (port);
        add (title);
        add (hint);

    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        //g2d.drawImage(Resources.cpu_secondary, buttonX, buttonY, buttonWidth, buttonHeight, null);

    }

    public void youWon () {
        title.setText("<html><div style='text-align: center;'>YOU WON!</div></html>");
        port.setText("<html><div style='text-align: center;'>Congratulations, your IQ is higher than the other humans.</div></html>");

        setVisible(true);
    }

    public void otherWon (String name) {
        title.setText("<html><div style='text-align: center;'>Player " + name + " WON!</div></html>");
        port.setText("<html><div style='text-align: center;'>You suck. Period.</div></html>");

        setVisible(true);
    }

}
