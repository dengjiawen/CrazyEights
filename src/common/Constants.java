package common;

import java.awt.*;
import java.util.HashMap;

public class Constants {

    public final static byte SPADES = 3;
    public final static byte HEARTS = 2;
    public final static byte DIAMONDS = 1;
    public final static byte CLUBS = 0;

    public final static byte DEFAULT = -1;
    public final static byte ERROR = -1;
    public final static byte SUCCESS = -2;

    public final static byte JACK = 11;
    public final static byte QUEEN = 12;
    public final static byte KING = 13;
    public final static byte ACE = 1;

    public final static byte CARD_IS_LARGER = 1;
    public final static byte CARD_IS_SMALLER = -1;
    public final static byte CARD_IS_SAME = 0;

    public final static byte VOTE_START = 0;
    public final static byte CANCEL_VOTE_START = 1;
    public final static byte PICKUP_ONLY = 2;
    public final static byte PLAY_CARD_PICKUP = 3;
    public final static byte PLAY_CARD_SKIP_TURN = 4;
    public final static byte SKIP_TURN_ONLY = 5;
    public final static byte VOTE_REMATCH_GGNORE = 6;
    public final static byte CANCEL_VOTE_REMATCH = 7;

    public final static String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
    public final static String[] ranks = {"null", "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    public final static String UIRes_path = "/ui/resources/";
    public final static String definition_path = "Definitions.plist";

    public final static String titleFont = ("<html><span style='font-size:20px;font-family:arial;font-weight:bold'>");
    public final static String bodyFont = ("<html><span style='font-size:12px;font-family:arial;font-weight:plain'>");

    public static HashMap <String, Integer> integer_parameters = new HashMap<>();

    public static int element (String name) {return getInt(name);}

    public static int getInt (String resource_name) {
        try {
            return integer_parameters.get(resource_name);
        } catch (NullPointerException error) {
            integer_parameters.put(resource_name,
                    Parse.parseInt(definition_path, resource_name));
            return integer_parameters.get(resource_name);
        }
    }

}