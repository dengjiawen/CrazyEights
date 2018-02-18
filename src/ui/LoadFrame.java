package ui;

import com.sun.awt.AWTUtilities;
import javax.swing.*;
import java.awt.*;

import common.Constants;
import ui.Resources;

/**
 * Created by freddeng on 2018-02-07.
 */
public class LoadFrame extends JFrame {

    private static final int width = 400;
    private static final int height = 110;

    private static LoadPanel load_panel;

    public LoadFrame () {

        setLayout(null);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.black);
        setSize(width, height);

        load_panel = new LoadPanel();
        add(load_panel);

        setVisible(true);

    }

    public static LoadPanel requestLoadPanelReference () {
        return load_panel;
    }

}
