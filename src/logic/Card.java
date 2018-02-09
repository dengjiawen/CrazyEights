package logic;

import common.Constants;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Card {

    private final byte suit;
    private final byte rank;

    Card () {
        this.suit = Constants.DEFAULT;
        this.rank = Constants.DEFAULT;
    }

    Card (byte suit, byte rank) {
        this.suit = suit;
        this.rank = rank;
    }

    boolean equals (Card c) {
        return this.suit == c.suit && this.rank == c.rank;
    }

    byte compare(Card c) {
        if (this.suit > c.suit) return Constants.CARD_IS_LARGER;
        if (this.suit < c.suit) return Constants.CARD_IS_SMALLER;

        if (this.rank > c.rank) return Constants.CARD_IS_LARGER;
        if (this.rank < c.rank) return Constants.CARD_IS_SMALLER;

        return Constants.CARD_IS_SAME;
    }

    public byte getSuit () {return suit;}

    public byte getRank () {return rank;}

    public String toString() {
        return (Constants.ranks[this.rank] + " of " + Constants.suits[this.suit]);
    }

}



