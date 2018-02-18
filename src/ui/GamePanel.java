package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import common.Console;
import common.Constants;
import logic.Card;

/**
 * Created by freddeng on 2018-01-26.
 */
class GamePanel extends JPanel {

    static int width = Constants.element("initWidth");
    static int height = Constants.element("initHeight");

    int curPlayerCount = 1;
    int curPlayerNum = Constants.ERROR;

    ArrayList<PlayerPanel> player;
    HandPanel hand;
    ButtonPanel buttons;
    StackPanel stack;

    protected GamePanel () throws Exception {

        super();

        setLayout(null);
        setBounds(0, 0, width, height);

        buttons = new ButtonPanel();

        stack = new StackPanel();

        player = new ArrayList<>();
        player.add(null);
        player.add(null);
        for (int i = 2; i <= Constants.element("MaxPlayer"); i ++) {
            player.add(new PlayerPanel("PLACEHOLDER", 0, i));
            player.get(i).setVisible(false);
            add(player.get(i));
        }

        add (buttons);
        add(stack);

    }

    public void addPlayer (String name, int playerNum) {

        int assigned_player_num = getAssignedNum(playerNum);

        curPlayerCount ++;
        Console.print(""+ assigned_player_num);

        player.get(assigned_player_num).updateInfo(name);
        player.get(assigned_player_num).setVisible(true);

        GameWindow.requestRef().repaint();
        GameWindow.requestRef().revalidate();
    }

    public void showHand () {
        hand = new HandPanel(GameWindow.requestRef().player.hand);
        hand.setVisible(true);

        add(hand);

        revalidate();
        repaint();
    }

    public void exitVoteStatus () {
        for (int i = 0; i < player.size(); i ++) {
            if (player.get(i) != null) player.get(i).exitVoteStatus();
        }
    }

    public void updateNumCards (int playerNum, int numCards) {
        Console.print("DEBUG: " + getAssignedNum(playerNum));
        player.get(getAssignedNum(playerNum)).updateNumCard(numCards);
    }

    public int getAssignedNum (int playerNum) {
        int this_player_num = GameWindow.requestRef().player.playerNum;
        Console.print("DEBUG TPN: " + this_player_num);
        int assigned_player_num = 0;

        if (playerNum > this_player_num) {
            assigned_player_num = playerNum - this_player_num + 1;
        } else {
            assigned_player_num = 6 - (this_player_num - playerNum) + 1;
            Console.print("DEBUG APN: " + assigned_player_num);
        }
        return assigned_player_num;
    }

    public void removePlayer () {
        for (int i = 0; i < player.size(); i ++) {
            if (player.get(i) != null) {
                this.remove(player.get(i));
            }
        }

        player = new ArrayList<>();
        player.add(null);
        player.add(null);
        for (int i = 2; i <= Constants.element("MaxPlayer"); i ++) {
            player.add(new PlayerPanel("PLACEHOLDER", 0, i));
            player.get(i).setVisible(false);
            add(player.get(i));
        }

        System.gc();

        curPlayerCount = 1;

        GameWindow.requestRef().revalidate();
        GameWindow.requestRef().repaint();
        GameWindow.requestRef().player.updateList();
    }

    public void updateReadyStatus (int playerNum, boolean ready) {
        player.get(getAssignedNum(playerNum)).voteReady(ready);
        GameWindow.requestRef().repaint();
    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.drawImage(Resources.background, 0, 0, width, height, null);

        int tableMarginW = (width - Resources.table.getWidth())/2;
        int tableMarginH = (height - Resources.table.getHeight())/2 + 50;

        g2d.drawImage(Resources.table, tableMarginW, tableMarginH,
                Resources.table.getWidth(), Resources.table.getHeight(), null);
    }

    public void startVotingSession () {
        buttons.setVisible(true);
        buttons.startVotingSession();
    }

    public void updateCurrentPlayer (int num) {
        player.get(getAssignedNum(num)).setCurrentPlayer(true);
        if (curPlayerNum != Constants.ERROR) player.get(getAssignedNum(curPlayerNum)).setCurrentPlayer(false);
        curPlayerNum = num;

        GameWindow.requestRef().revalidate();
        GameWindow.requestRef().repaint();
    }

    public void updateActiveCard (Card card) {
        stack.lastActiveCardRef = stack.activeCardRef;
        stack.activeCardRef = Resources.cards[card.getSuit()][card.getRank()];

        GameWindow.requestRef().repaint();
    }

    public void allowToPlay () {
        GameWindow.requestRef().player.hand.findPlayable();
        hand.allowToPlay(true);
        buttons.startPlayingSession();
    }

    public void stopPlay () {
        hand.allowToPlay(false);
    }

    public void activatePlayButton (boolean doActivate) {
        buttons.activatePlayButton(doActivate);
    }

    public void goodMove () {
        GameWindow.player.hand.remove(hand.selected);
        buttons.stopPlayingSession();
        hand.allowToPlay(false);
        repaint();
    }

}
