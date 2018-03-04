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
 * Constants.java
 *-----------------------------------------------------------------------------
 * This is a specialized java class designed to host the necessary constants.
 *-----------------------------------------------------------------------------
 */

package common;

import java.util.HashMap;

public class Constants {

    // Constants defining numerical representations of suits
    public final static byte SPADES = 3;
    public final static byte HEARTS = 2;
    public final static byte DIAMONDS = 1;
    public final static byte CLUBS = 0;

    // Constants defining error/default/success messages
    public final static byte DEFAULT = -1;
    public final static byte ERROR = -1;
    public final static byte SUCCESS = -2;

    // Constants defining special ranks
    public final static byte JACK = 11;
    public final static byte QUEEN = 12;
    public final static byte KING = 13;
    public final static byte ACE = 1;

    // Constants defining card size comparison results
    public final static byte CARD_IS_LARGER = 1;
    public final static byte CARD_IS_SMALLER = -1;
    public final static byte CARD_IS_SAME = 0;

    // Constants defining ButtonPanel states
    public final static byte VOTE_START = 0;
    public final static byte CANCEL_VOTE_START = 1;
    public final static byte PICKUP_ONLY = 2;
    public final static byte PLAY_CARD_PICKUP = 3;
    public final static byte PLAY_CARD_SKIP_TURN = 4;
    public final static byte SKIP_TURN_ONLY = 5;
    public final static byte VOTE_REMATCH_GGNORE = 6;
    public final static byte CANCEL_VOTE_REMATCH = 7;

    // Constant, placeholder for when no card is selected
    public final static byte NO_CARD_SELECTED = -1;

    // Arrays to assign String representation to suits and ranks
    public final static String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
    public final static String[] ranks = {"null", "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    // Constant defining the import path of UI elements
    public final static String UIRes_path = "/ui/resources/";
    // Constant defining the location of the plist definition
    public final static String definition_path = "Definitions.plist";

    // Constants defining CSS formatting of text messages
    public final static String titleFont = ("<html><span style='font-size:20px;font-family:arial;font-weight:bold'>");
    public final static String bodyFont = ("<html><span style='font-size:12px;font-family:arial;font-weight:plain'>");

    // HashMap to keep track of constants already requested by element() and getInt()
    public static HashMap <String, Integer> integer_parameters = new HashMap<>();

    /**
     * Performs the same function as getInt().
     * Method had been DEPRECATED | Use getInt() instead.
     * @param resource_name    the name of the resource to be retrieved
     * @return      requested integer data from plist definition
     */
    @Deprecated
    public static int element (String resource_name) {
        Console.printGeneralMessage("This method had been deprecated. " +
                "Use getInt() instead.", Constants.class.getName());

        return getInt(resource_name);
    }

    /**
     * Method first checks integer_parameters for the requested resource,
     * and return the resource if found. (less resource usage)
     * If not found, a NullPointerException is thrown. It will be
     * caught, and the resource will be parsed from the plist file,
     * saved to integer_parameters, and returned.
     * @param resource_name    the name of the resource to be retrieved
     * @return      requested integer data from plist definition
     */
    public static int getInt (String resource_name) {
        Console.printGeneralMessage("Resource " + resource_name + " requested. " +
                "Will attempt to retrieve from existing values.", Constants.class.getName());

        try {
            return integer_parameters.get(resource_name);
        } catch (NullPointerException e) {
            Console.printGeneralMessage("Resource " + resource_name + " cannot be found in" +
                            " existing values. Parsing value." , Constants.class.getName());

            integer_parameters.put(resource_name,
                    Parse.parseInt(definition_path, resource_name));
            return getInt(resource_name);
        }
    }

}