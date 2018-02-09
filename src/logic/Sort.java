package logic;

import common.Constants;

import java.util.ArrayList;

/**
 * Created by freddeng on 2018-02-07.
 */
public class Sort {

    static Deck mergeSort (Deck deck) {

        if (deck.size() <= 1) {
            return deck;
        }

        int middleIndex = (deck.size() - 1)/2;

        Deck sub1 = deck.subdeck(0, middleIndex);
        Deck sub2 = deck.subdeck(middleIndex + 1, deck.size() - 1);

        sub1 = mergeSort(sub1);
        sub2 = mergeSort(sub2);

        return merge(sub1, sub2);

    }

    private static Deck merge (Deck d1, Deck d2) {

        Deck result = new Deck(false);
        int arrayLength = d1.size() + d2.size();

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

        return result;

    }

    public static void selectionSort (Hand hand) {

        for (int j = 0; j < hand.size(); j ++) {
            hand.swap(findSmallestCardIndex(hand, j), j);
        }

    }

    public static int findSmallestCardIndex (Hand hand, int startIndex) {

        int smallestIndex = startIndex;

        for (int i = startIndex; i < hand.size(); i ++) {
            if (hand.get(i).compare(hand.get(smallestIndex)) == Constants.CARD_IS_SMALLER) {
                smallestIndex = i;
            }
        }

        return smallestIndex;
    }

}
