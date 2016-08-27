package org.gio.jsrt.util;

import java.util.List;

/**
 * String utility functions.
 * 
 *
 */
public class StringUtils {
    private StringUtils() {
    }
    
    /**
     * Joins the elements in the strings with delimiter.
     * 
     * @param strings the strings
     * @param delimiter the delimiter
     * @return the string
     */
    public static String join(List<String> strings, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i < strings.size()-1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }
}
