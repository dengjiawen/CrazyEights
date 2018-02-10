package logic;

import java.util.ArrayList;
import common.Constants;

/**
 * Created by freddeng on 2018-02-05.
 */

class Deck {

    private ArrayList<Card> cards;

    Deck () {
        this(true);
    }

    Deck (boolean fill) {
        if (fill) {
            fill();
        } else {
            cards = new ArrayList<>();
        }
    }

    public void fill() {
        cards = new ArrayList<>();

        for (byte index = 0, suit = 0, rank = 1; index < 52; index ++, suit ++, rank ++){
            add(new Card (suit, rank));

            if (suit == 3) suit = 0 - 1;
            if (rank == 13) rank = 1 - 1;
        }

        Sort.mergeSort(this);
    }

    void swap(int index1, int index2) {
        Card tempRef = get(index1);

        cards.set(index1, get(index2));
        cards.set(index2, tempRef);
    }

    void shuffle () {

        for (int i = 0; i < cards.size(); i++) {
            int swapIndex = (int)Math.floor(Math.random() * (cards.size() - i) + i);

            swap(i, swapIndex);
        }

    }

    void add (Card card) {
        cards.add(card);
    }

    void remove (Card card) {
        cards.remove(card);
    }

    void remove (int low, int high) {
        for (int i = low; i <= high; i ++) {
            remove(cards.get(i));
        }
    }

    public int size () {
        return cards.size();
    }

    int find (Card card) {
        for (int i = 0; i < cards.size(); i ++) {
            if (get(i).equals(card)) return i;
        }
        return Constants.ERROR;
    }

    public Card get (int index) {
        return cards.get(index);
    }

    Deck subdeck (int low, int high) {

        Deck sub = new Deck(false);

        for (int i = low; i <= high; i ++) {
            sub.add(get(i));
        }

        return sub;

    }

    boolean isEmpty (int index) {
        return index >= cards.size();
    }

}




