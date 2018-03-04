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
 * Hand.java
 *-----------------------------------------------------------------------------
 * This is a class representing the Hand object. It inherits the Deck class.
 *-----------------------------------------------------------------------------
 */

package logic;

import common.Constants;
import ui.References;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Hand extends Deck {

    // create a weak reference towards player object
    // to prevent errors in garbage collection
    private static WeakReference<Player> player;

    // integer of the index selected by the user
    private int selected_index;
    // Arraylist holding booleans indicating whether
    // cards in the hand is playable
    private ArrayList<Boolean> isPlayable;

    /**
     * Default constructor
     */
    public Hand () {
        super(false);

        selected_index = Constants.NO_CARD_SELECTED;
        isPlayable = new ArrayList<>();

        References.updateReferences();
    }

    /**
     * Method to sort hand and find
     * playable cards after modifying
     * hand.
     */
    public void update () {

        // clear ArrayList and refill
        isPlayable.clear();
        // sort cards via selectionSort
        Sort.selectionSort(this);

        for (int i = 0; i < size(); i++) {
            isPlayable.add(false);
        }
    }

    /**
     * Reset all parameter of the hand class after
     * the player's turn is over.
     */
    public void reset () {
        selected_index = Constants.NO_CARD_SELECTED;
        for (int i = 0; i < isPlayable.size(); i ++) {
            isPlayable.set(i, false);
        }
    }

    /**
     * Method for selecting a card when playing.
     * (if called again on the same index, that
     * index will be unselected)
     * @param index index of the card selected
     */
    public void select (int index) {
        if (selected_index == index) selected_index = Constants.NO_CARD_SELECTED;
        else selected_index = index;
    }

    /**
     * Overloaded fill method from Deck class
     * updates isPlayable and sort cards
     * after filling.
     */
    public void fill () {
        super.fill();
        update();
    }

    /**
     * Method that returns a boolean to indicate
     * if hand is empty (used for winning conditions)
     * @return  boolean
     */
    public boolean isEmpty () {
        return size() == 0;
    }

    /**
     * Method that finds all playable cards in a hand
     */
    public void findPlayable () {

        // update, then find playable cards
        update();

        for (int i = 0; i < size(); i++) {
            if (isPlayable(get(i))) isPlayable.set(i, true);
        }

    }

    /**
     * Get the current selected index.
     * @return  selected index
     */
    public int getSelectedIndex () {
        return selected_index;
    }

    /**
     * Get the selected card.
     * @return selected card if exists, null if not
     */
    public Card getSelectedCard () {
        if (selected_index != Constants.NO_CARD_SELECTED) return get(selected_index);
        else return null;
    }

    /**
     * Method that determines if a card is playable.
     * @param card  target card
     * @return      boolean of whether card is playable
     */
    public boolean isPlayable (Card card) {

        Card activeCard = player.get().getActiveCard();
        if (card.getRank() == 8) {
            return true;    // special case; 8 is always playable
        } else if (card.getSuit() == activeCard.getSuit()) {
            return true;
        } else if (card.getRank() == activeCard.getRank()) {
            return true;
        }

        return false;
    }

    /**
     * Method that determines if a card at an index
     * is playable.
     * This is an extension of
     * isPlayable (Card card)
     * @param index index of the card
     * @return      boolean of whether card is palyable
     */
    public boolean isPlayable (int index) {
        try {
            return isPlayable.get(index);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Method that converts a deck into a hand.
     * Used for converting subdecks into hands.
     * @param deck  the original deck
     * @return      the converted Hand
     */
    public static Hand valueOf (Deck deck) {
        Hand temp_deck = new Hand();

        for (int i = 0; i < deck.size(); i ++) {
            temp_deck.add(deck.get(i));
        }

        return temp_deck;
    }

    /**
     * Method that updates all weak references
     * of this class.
     */
    public static void updateReferences () {
        player = new WeakReference<>(References.player);
    }

}




