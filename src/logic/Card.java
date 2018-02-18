/**
 * Copyright 2018 (C) Jiawen Deng. All rights reserved.
 *
 * This document is the property of Jiawen Deng.
 * It is considered confidential and proprietary.
 *
 * This document may not be reproduced or transmitted in any form,
 * in whole or in part, without the express written permission of
 * Jiawen Deng.
 *
 *-----------------------------------------------------------------------------
 * Card.java
 *-----------------------------------------------------------------------------
 * This is a class representing the Card object. Carries two instance
 * integers, representing the suit and rank of the card.
 *-----------------------------------------------------------------------------
 */

package logic;

import common.Console;
import common.Constants;

public class Card {

    private final byte suit;    // card suit
    private final byte rank;    // card rank

    /**
     * Default Constructor
     * (no parameter)
     */
    Card () {
        this.suit = Constants.DEFAULT;
        this.rank = Constants.DEFAULT;
    }

    /**
     * Constructor
     * @param suit  assigned suit
     * @param rank  assigned rank
     */
    Card (byte suit, byte rank) {
        this.suit = suit;
        this.rank = rank;

        Console.printGeneralMessage("New card created with value " + this.toString(),
                Card.class.getName());
    }

    /**
     * Method to check equality of Card objects.
     * @param card  card to be compared to
     * @return  boolean
     */
    public boolean equals (Card card) {
        return this.suit == card.suit && this.rank == card.rank;
    }

    /**
     * Method to compare the value of Cards.
     * Used for sorting only.
     * @param card  card to be compared to
     * @return  byte signifying comparison
     *          (check common.Constants for details)
     */
    public byte compare(Card card) {

        if (equals(card)) return Constants.CARD_IS_SAME;

        if (this.suit > card.suit) return Constants.CARD_IS_LARGER;
        if (this.suit < card.suit) return Constants.CARD_IS_SMALLER;

        if (this.rank > card.rank) return Constants.CARD_IS_LARGER;
        if (this.rank < card.rank) return Constants.CARD_IS_SMALLER;

        return Constants.ERROR;
    }

    /**
     * Method that returns the suit of the card.
     * Read-only, not writtable (object protection)
     * @return  integer representation of suit
     */
    public byte getSuit () {return suit;}

    /**
     * Method that returns the rank of the card.
     * Read-only, not writtable (object protection)
     * @return  integer representation of rank
     */
    public byte getRank () {return rank;}

    /**
     * Overriden toString method.
     * For common.Console usages only.
     * @return  string representaiton of Card object
     */
    @ Override
    public String toString() {
        return (Constants.ranks[this.rank] + " of " + Constants.suits[this.suit]);
    }

}



