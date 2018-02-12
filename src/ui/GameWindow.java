package ui;

import common.Constants;
import logic.Player;
import logic.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by freddeng on 2018-01-26.
 */

public class GameWindow extends JFrame {

    private static int width = Constants.element("initWidth");
    private static int height = Constants.element("initHeight");

    private static GameWindow frameRef;
    private static ShadePanel shade;
    private static GamePanel panel;
    private static TitlePanel title;
    private static MainMenuPanel main_menu;
    private static HostPanel host;

    Player player;

    public GameWindow () throws Exception {

        super();

        setSize(width, height);
        setLayout(null);
        setUndecorated(true);
        setResizable(false);
        setLocationRelativeTo(null);

        Resources.loadImageAssets();

        panel = new GamePanel();
        title = new TitlePanel();
        shade = new ShadePanel();
        main_menu = new MainMenuPanel();
        host = new HostPanel();

        add(host);
        add(main_menu);
        add(title);
        add(shade);
        add(panel);

        setVisible(true);
        setShade(true);

    }

    void minimize () {
        setState(Frame.ICONIFIED);
    }

    public void setShade (boolean isShaded) {
        if (isShaded) shade.setVisible(true);
        else shade.setVisible(false);
    }

    public int getHeight () {
        return height;
    }

    public int getWidth () {
        return width;
    }

    public int host () {
        int portNumber = 0;

        while (true) {
            String input = JOptionPane.showInputDialog(Constants.titleFont + "Enter a Port Number\n" +
                    Constants.bodyFont + "In the text field above, enter the port number that will be used.\n" +
                    Constants.bodyFont + "Other players will need this number to join your game.", "Enter Port Number");
            try {
                if (input != null) {
                    portNumber = Integer.parseInt(input);
                } else {
                    return Constants.ERROR;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(shade, "An invalid port number was entered. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (portNumber <= 25565 && portNumber > 0) {
                try {
                    Server.init(portNumber);
                    break;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(shade, "This port cannot be reserved for the game.\n" +
                            "Please try a different port.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        try {
            player = new Player("localhost", portNumber, "John");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(shade, "A catastrophic network error had occured.\n" +
                    "Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            try {
                Server.listener.close();
            } catch (IOException f) {}
            return Constants.ERROR;
        }

        host.updatePort(portNumber);

        return Constants.SUCCESS;
    }

    public int connect () {

        int port = 0;

        String name;
        String ip = "";

        while (true) {
            String input = JOptionPane.showInputDialog("Enter game IP address:");
            try {
                if (input != null) {
                    ip = input;
                    break;
                } else {
                    return Constants.ERROR;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(shade, "An invalid IP address was entered. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (true) {
            String input = JOptionPane.showInputDialog("Enter game port:");
            try {
                if (input != null) {
                    port = Integer.parseInt(input);
                    break;
                } else {
                    return Constants.ERROR;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(shade, "An invalid port was entered. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (true) {
            String input = JOptionPane.showInputDialog("Enter a nickname: ");

            if (input != null) {
                name = input;
                break;
            } else {
                return Constants.ERROR;
            }
        }

        try {
            player = new Player(ip, port, name);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(shade, "A catastrophic network error had occured.\n" +
                    "Please try again.", "Error", JOptionPane.ERROR_MESSAGE);

            return Constants.ERROR;
        }

        return Constants.SUCCESS;
    }

    public void startVotingSession () {
        panel.startVotingSession();
    }

    public void startGame () {
        panel.showHand();
        panel.buttons.setVisible(false);
        panel.exitVoteStatus();

    }

    public void updateNumCards (int playerNum, int numCards) {
        panel.updateNumCards(playerNum, numCards);
    }

    public void updateHand () {
        panel.hand.update();
    }

    public void addPlayer (String name, int playerNum) {
        panel.addPlayer(name, playerNum);
    }

    public void removePlayer () {
        panel.removePlayer();
    }

    public void updateReadyStatus (int playerNum, boolean ready) {
        panel.updateReadyStatus (playerNum, ready);
    }

    public static void setRef (GameWindow ref) {
        frameRef = ref;
    }

    public static GameWindow requestRef () {
        return frameRef;
    }

}
