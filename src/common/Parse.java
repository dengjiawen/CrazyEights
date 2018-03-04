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
 * Parse.java
 *-----------------------------------------------------------------------------
 * This is a specialized java class designed to parse content from
 * property list files (.plist files) in XML format.
 *-----------------------------------------------------------------------------
 */

package common;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.io.IOException;

public class Parse {

    //Constant String used for console output.
    private static final String class_name = "common.Parse";

    /**
     * Parse strings from XML entries.
     * @param plist_name     name of the plist file
     * @param resource_name  name of the requested resource
     * @return  parsed content (string)
     */
    public static String parseString (String plist_name, String resource_name) {

        //Parse XML tree structure, transverse to find requested resource, parse and return
        try {
            Document XMLTree = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().parse(Parse.class.getResourceAsStream(plist_name));
            XMLTree.getDocumentElement().normalize();

            Console.printGeneralMessage("Found resource [" + resource_name + "] in plist [" + plist_name +"]", class_name);

            return XMLTree.getElementsByTagName(resource_name).item(0).getTextContent();
        } catch (SAXException e) {
            Console.printErrorMessage("[SAXException]" + e.getMessage(), class_name);
        } catch (IOException e) {
            Console.printErrorMessage("[IOException]" + e.getMessage(), class_name);
        } catch (ParserConfigurationException e) {
            Console.printErrorMessage("[ParserConfigurationException]" + e.getMessage(), class_name);
        }

        Console.printErrorMessage("Resource " + resource_name + " is not found in plist " + plist_name, class_name);

        //If file is not found, return null.
        return null;

    }

    /**
     * Parse integer from XML entries, using parseString
     * as a helper method.
     * @param plist_name     name of the plist file
     * @param resource_name  name of the requested resource
     * @return  parsed content (integer)
     */
    public static int parseInt (String plist_name, String resource_name) {
        return Integer.parseInt(parseString(plist_name, resource_name));
    }

    /**
     * Parse float from XML entries, using parseString
     * as a helper method.
     * @param plist_name     name of the plist file
     * @param resource_name  name of the requested resource
     * @return  parsed content (float)
     */
    public static float parseFloat (String plist_name, String resource_name) {
        return Float.parseFloat(parseString(plist_name, resource_name));
    }

}
