package common;

import logic.Card;
import logic.Hand;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Misc {

    public static boolean isBetween (int a, int b, int c) {
        return a <= b && b <= c;
    }

    public static void deepRemove (ArrayList<Card> cards, Card card) {
        for (int i = 0; i < cards.size(); i ++) {
            if (cards.get(i).equals(card)) {
                cards.remove(card);
            }
        }
    }

    public static void deepRemove (Hand cards, Card card) {
        for (int i = 0; i < cards.size(); i ++) {
            if (cards.get(i).equals(card)) {
                cards.remove(card);
            }
        }
    }

}