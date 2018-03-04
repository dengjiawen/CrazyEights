/**
 * Copyright 2018 (C) Jiawen Deng. All rights reserved.
 *
 * This document is the property of Jiawen Deng.
 * It is considered confidential and proprietary.
 *
 * This document may not be reproduced or transmitted in any form,
 * in whole or in part, without the express written permission of
 * Jiawen Deng. (I-LU-V-EH)
 *
 * Roses are red,
 * Violets are blue.
 * Unexpected '{'
 * On line 32.
 *
 *-----------------------------------------------------------------------------
 * Misc.java
 *-----------------------------------------------------------------------------
 * This java class contains some useful helper methods.
 *-----------------------------------------------------------------------------
 */

package common;

import logic.Card;
import logic.Hand;

import java.util.ArrayList;

public class Misc {

    /**
     * Returns a boolean indicating if b is between a and c.
     * @param a     lower range
     * @param b     target number
     * @param c     higher range
     * @return      boolean
     */
    public static boolean isBetween (int a, int b, int c) {
        Console.printDebugMessage("Number is compared. " +
                a + " | " + b + " | " + c + ".", Misc.class.getName());

        return a <= b && b <= c;
    }

    /**
     * Removes a card from an arraylist. The actual value of
     * the card is checked, not the hexadecimal address.
     * @param cards arraylist
     * @param card  Card object
     */
    public static void deepRemove (ArrayList<Card> cards, Card card) {
        Console.printDebugMessage("Removing card " + card + " from " + cards + ".", Misc.class.getName());

        for (int i = 0; i < cards.size(); i ++) {
            if (cards.get(i).equals(card)) {
                cards.remove(card);
            }
        }
    }

    /**
     * Removes a card from a hand. The actual value of
     * the card is checked, not the hexadecimal address.
     * @param cards Hand
     * @param card  Card object
     */
    public static void deepRemove (Hand cards, Card card) {
        Console.printDebugMessage("Removing card " + card + " from " + cards + ".", Misc.class.getName());

        for (int i = 0; i < cards.size(); i ++) {
            if (cards.get(i).equals(card)) {
                cards.remove(card);
            }
        }
    }

}