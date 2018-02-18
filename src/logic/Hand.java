package logic;

import common.Console;
import ui.GameWindow;

import java.util.ArrayList;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Hand extends Deck {

    public int selectedIndex;
    ArrayList<Boolean> isPlayable;

    public Hand () {
        super(false);

        selectedIndex = -1;
        isPlayable = new ArrayList<>();
    }

    void play (Card card) {
        remove (card);
    }



    public void fill () {
        super.fill();
        update();
    }

    public void add (Card card) {
        super.add(card);
        isPlayable.add(false);
        GameWindow.requestRef().repaint();
    }

    boolean isEmpty () {
        return size() == 0;
    }

    public void update () {
        for (int i = 0; i < size(); i++) {
            isPlayable.add(new Boolean(false));
        }
    }

    public void findPlayable () {
        for (int i = 0; i < size(); i ++) {
            Card eval_card = get(i);
            if (isPlayable(eval_card)) isPlayable.set(i, true);
        }
    }

    public void select (int index) {
        if (selectedIndex == index) selectedIndex = -1;
        else selectedIndex = index;
    }

    public boolean isPlayable (int index) {
        return isPlayable.get(index);
    }

    public boolean isPlayable (Card card) {

        Console.print(this.toString());

        Card activeCard = GameWindow.requestRef().player.activeCard;
        if (card.getRank() == 8) {
            return true;
        } else if (card.getSuit() == activeCard.getSuit()) {
            return true;
        } else if (card.getRank() == activeCard.getRank()) {
            return true;
        }

        return false;
    }

    public static Hand valueOf (Deck deck) {
        Hand temp_deck = new Hand();

        for (int i = 0; i < deck.size(); i ++) {
            temp_deck.add(deck.get(i));
        }

        return temp_deck;
    }

}




