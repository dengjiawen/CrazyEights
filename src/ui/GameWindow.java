package ui;

import common.Constants;
import logic.Server;

import javax.swing.*;
import java.awt.*;

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

        return Constants.SUCCESS;
    }

    public static void setRef (GameWindow ref) {
        frameRef = ref;
    }

    static GameWindow requestRef () {
        return frameRef;
    }

}
