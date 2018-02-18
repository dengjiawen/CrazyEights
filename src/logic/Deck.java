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
 * Deck.java
 *-----------------------------------------------------------------------------
 * This is a class representing the Deck object. Carries an ArrayList of Card
 * object, representing cards in a deck.
 *-----------------------------------------------------------------------------
 */

package logic;

import java.util.ArrayList;
import java.util.Objects;

import common.Console;
import common.Constants;

class Deck {

    private ArrayList<Card> cards;  // ArrayList of Cards

    /**
     * Default constructor
     * (no parameter)
     * Calls on overloaded constructor,
     * fills deck by default
     */
    Deck () {
        this(true);
    }

    /**
     * Constructor
     * @param doFill    boolean of whether to fill deck or not
     */
    Deck (boolean doFill) {

        cards = new ArrayList<>();

        if (doFill) {
            fill();
        }

        Console.printGeneralMessage("New deck created, fill is " + doFill,
                Card.class.getName());
    }

    /**
     * Method that fills deck with a standard set
     * of 52 poker Cards (excl. Jokers)
     */
    void fill() {

        for (byte index = 0, suit = 0, rank = 1; index < 52; index ++, suit ++, rank ++){
            add(new Card (suit, rank));

            if (suit == 3) suit = 0 - 1;
            if (rank == 13) rank = 1 - 1;
        }

        System.out.println("size" + size());

        Sort.selectionSort(this);

        for (int i = 0; i < size(); i ++) {
            System.out.println(get(i));
        }

        // Sort the filled deck
        //Sort.mergeSort(this);

        System.out.println("size" + size());
    }

    void print () {
        for (int i = 0; i < size(); i ++) {
            System.out.println(get(i));
        }
    }

    /**
     * Method that swaps two Cards in the ArrayList
     * @param index1    index of first Card
     * @param index2    index of second Card
     */
    void swap(int index1, int index2) {
        Card tempRef = get(index1);     // Store a temporary ref. of Card 1

        cards.set(index1, get(index2)); // Move Card 2 to Card 1's location
        cards.set(index2, tempRef);     // Move temporary ref. to Card 2's location
    }

    /**
     * Method that shuffles the deck
     */
    void shuffle () {

        /*
         * Transverse through the deck
         * and swap cards at random
         */
        for (int i = 0; i < cards.size(); i++) {
            int swapIndex = (int)Math.floor(Math.random() * (cards.size() - i) + i);

            swap(i, swapIndex);
        }

    }

    /**
     * Method that adds a Card.
     * @param card  Card to be added.
     */
    void add (Card card) {
        cards.add(card);
    }

    void set (int index, Card card) {
        cards.set(index, card);
    }

    /**
     * Method that removes a Card.
     * @param card  Card to be removed.
     */
    public void remove (Card card) {
        cards.remove(card);
    }

    public void remove (int index) {
        cards.remove(index);
    }

    public void removeNullPointers () {
        while(cards.remove(null));
    }

    /**
     * Method that removes a Card within a
     * range of indexes. (inclusive)
     * @param low   lower index
     * @param high  higher index
     */
    public void remove (int low, int high) {
        for (int i = low; i <= high; i ++) {
            set(i, null);
        }

        removeNullPointers();

    }

    /**
     * Method that returns the number of Cards
     * in the deck.
     * @return  integer representing number of Cards
     */
    public int size () {
        return cards.size();
    }

    /**
     * Method that returns the Card object at index.
     * @param index index of the desired Card
     * @return  Card object appointed by index
     */
    public Card get (int index) {
        return cards.get(index);
    }

    /**
     * Method that returns the index of the
     * target Card object.
     * Returns Constants.ERROR if card not found.
     * Only object references are compared. (shallow distinction)
     * @param card  target Card object
     * @return  integer of Card index
     */
    int indexOf (Card card) {
        return cards.indexOf(card);
    }

    /**
     * Method that returns the index of the
     * target Card object.
     * Returns Constants.ERROR if card not found.
     * Individual ranks and suits are compared.
     * (deep distinction)
     * @param card  target Card object
     * @return  integer of Card index
     */
    int find (Card card) {
        for (int i = 0; i < cards.size(); i ++) {
            if (get(i).equals(card)) return i;
        }
        return Constants.ERROR;
    }

    /**
     * Method that returns a part of a deck.
     * @param low
     * @param high
     * @return
     */
    Deck subdeck (int low, int high) {

        Deck temp_deck = new Deck(false);

        for (int i = low; i <= high; i ++) {
            temp_deck.add(get(i));
            remove(get(i));
        }

        return temp_deck;

    }

    boolean isEmpty (int index) {
        return index >= cards.size();
    }

}




