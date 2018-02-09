package ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Constants;
import logic.Hand;
import logic.Player;
import logic.Server;

/**
 * Created by freddeng on 2018-01-26.
 */
class ShadePanel extends JPanel {

    static int width = Constants.element("initWidth");
    static int height = Constants.element("initHeight");

    protected ShadePanel () throws Exception {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);
        setBackground(new Color(0,0,0, 200));

    }

    public void setVisible (boolean isVisible) {
        super.setVisible(isVisible);

        if (isVisible) grabFocus();
    }

}
