package common;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;

import java.awt.*;

/**
 * Created by freddeng on 2017-10-06.
 */
public class Parse {

    public static String parseString (String pListName, String resourceName) {

        try {

            DocumentBuilderFactory XMLParser = DocumentBuilderFactory.newInstance();
            DocumentBuilder XMLBuilder = XMLParser.newDocumentBuilder();
            Document XMLTree = XMLBuilder.parse(Parse.class.getResourceAsStream(pListName));

            XMLTree.getDocumentElement().normalize();

            return XMLTree.getElementsByTagName(resourceName).item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0";

    }

    public static int parseInt (String pListName, String resourceName) {
        return Integer.parseInt(parseString(pListName, resourceName));
    }

    public static boolean parseBool (String pListName, String resourceName) {
        return parseInt (pListName, resourceName) == 1;
    }

    public static Color parseColor (String pListName, String resourceName) {
        return Color.decode("#" + parseString(pListName, resourceName));
    }

}
