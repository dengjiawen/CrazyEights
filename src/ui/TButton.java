package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import common.Console;
import common.Constants;

/**
 * Created by freddeng on 2018-01-26.
 */
class TButton extends JButton {

    private String dimmableAsset;

    protected TButton () {

        super();

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

    }

    protected TButton (int x, int y, int width, int height) {

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setBounds(x, y, width, height);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    protected TButton (int x, int y, int width, int height, String dimmable_Asset_Name) {

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setBounds(x, y, width, height);

        dimmableAsset = dimmable_Asset_Name;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Resources.dimImage(dimmableAsset, true);
                getRootPane().repaint();

                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Resources.dimImage(dimmableAsset, false);
                getRootPane().repaint();

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

    }
}
