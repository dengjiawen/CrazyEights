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
 * Sort.java
 *-----------------------------------------------------------------------------
 * This class contains all the sorting algorithms for sorting deck and hand.
 *-----------------------------------------------------------------------------
 */

package logic;

import common.Constants;

public class Sort {

    /**
     * Sorting by mergeSort (recursive) (DEPRECATED)
     * @param deck  deck to be sorted
     * @return      sorted deck
     */
    @ Deprecated
    public static Deck mergeSort (Deck deck) {

        // base case
        if (deck.size() <= 1) {
            return deck;
        }

        // find middle index
        int middleIndex = (deck.size() - 1)/2;

        // split deck into two subdecks
        Deck sub1 = deck.subdeck(0, middleIndex);
        Deck sub2 = deck.subdeck(middleIndex + 1, deck.size() - 1);

        // recursively sort each subdeck
        sub1 = mergeSort(sub1);
        sub2 = mergeSort(sub2);

        // merge sorted decks
        return merge(sub1, sub2);

    }

    /**
     * Merge sorted decks (d1 and d2)
     * @param d1    first subdeck
     * @param d2    second subdeck
     * @return      merged decks
     */
    private static Deck merge (Deck d1, Deck d2) {

        Deck result = new Deck(false);
        int arrayLength = d1.size() + d2.size();

        // go through the two decks of card, pull cards from d1 and d2
        // to add to the new deck based on which card is larger
        // in value.
        for (int i = 0, index_1 = 0, index_2 = 0; i < arrayLength; i ++) {

            if (d1.isEmpty(index_1)) {
                result.add(d2.get(index_2 ++));
            } else if (d2.isEmpty(index_2)) {
                result.add(d1.get(index_1 ++));
            } else {
                if (d1.get(index_1).compare(d2.get(index_2)) == Constants.CARD_IS_LARGER) {
                    result.add(d2.get(index_2 ++));
                } else {
                    result.add(d1.get(index_1 ++));
                }
            }

        }

        // return merged deck
        return result;

    }

    /**
     * Sorting by selectionSort (iterative modifier method)
     * @param hand  hand to be sorted (modified)
     */
    public static void selectionSort (Hand hand) {

        // go through the hand, swap cards based on size (find smallest)
        for (int j = 0; j < hand.size(); j ++) {
            hand.swap(findSmallestCardIndex(hand, j), j);
        }

    }

    /**
     * Sorting by selectionSort (iterative modifier method)
     * @param deck  deck to be sorted (modified)
     */
    public static void selectionSort (Deck deck) {

        // go through the deck, swap cards based on size
        for (int j = 0; j < deck.size(); j ++) {
            deck.swap(findSmallestCardIndex(deck, j), j);
        }

    }

    /**
     * Find the index of the smallest card in a range (for selection sort)
     * @param hand         target hand
     * @param startIndex   starting index of the range
     * @return  integer of the smallest index
     */
    public static int findSmallestCardIndex (Hand hand, int startIndex) {

        int smallestIndex = startIndex;

        // if card is smaller, set smallestIndex equal to its index
        for (int i = startIndex; i < hand.size(); i ++) {
            if (hand.get(i).compare(hand.get(smallestIndex)) == Constants.CARD_IS_SMALLER) {
                smallestIndex = i;
            }
        }

        return smallestIndex;
    }

    /**
     * Find the smallest card in a range (for selection sort)
     * @param deck          target deck
     * @param startIndex    starting index of the range
     * @return  integer of the smallest index
     */
    public static int findSmallestCardIndex (Deck deck, int startIndex) {

        // works the same way as findSmallestIndex (Hand, int)

        int smallestIndex = startIndex;

        for (int i = startIndex; i < deck.size(); i ++) {
            if (deck.get(i).compare(deck.get(smallestIndex)) == Constants.CARD_IS_SMALLER) {
                smallestIndex = i;
            }
        }

        return smallestIndex;
    }

}
