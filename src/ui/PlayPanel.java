package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Constants;
import common.Misc;
import logic.Hand;
import logic.Sort;

/**
 * Created by freddeng on 2018-01-26.
 */
class PlayPanel extends JPanel {


    protected PlayPanel (Hand currentHand) {

        super();

        setLayout(null);
        setOpaque(false);


    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

    }

}
