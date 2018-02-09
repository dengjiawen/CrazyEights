package common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {

    public static void print (String message) {
        System.out.println(getDate() + " -> " + message);
    }

    private static String getDate () {
        DateFormat date_format = new SimpleDateFormat("HH:mm:ss:SSS");
        Date date_object = new Date();

        return date_format.format(date_object);
    }

}