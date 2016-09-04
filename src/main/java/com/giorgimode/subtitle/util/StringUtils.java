package com.giorgimode.subtitle.util;

import com.giorgimode.subtitle.api.SubtitleUnit;

import java.util.List;

/**
 * String utility functions.
 */
public class StringUtils {

    public static final String REGEX_SYNTAX_TAGS = "\\<.*?\\>";
    public static final String REGEX_SPACE = "\\s+";

    private StringUtils() {
    }

    /**
     * Joins the elements in the strings with delimiter.
     *
     * @param strings   the strings
     * @param delimiter the delimiter
     * @return the string
     */
    public static String join(List<String> strings, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i < strings.size() - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String[][] convertSubtitleUnit(SubtitleUnit subtitleUnit) {
        List<String> subtitleLines = subtitleUnit.text;
        int numberOfLines = subtitleLines.size();
        String[][] subtitleWords = new String[numberOfLines][];
        for (int i = 0; i < numberOfLines; i++) {
            String currentLine = subtitleLines.get(i);
            //remove all highlighting tags like <b> and split them via space
            String[] words = currentLine.replaceAll(REGEX_SYNTAX_TAGS, "").split(REGEX_SPACE);
            for (int j = 0; j < words.length; j++) {
                words[j] = words[j].replaceAll("[^a-zA-Z-']", "");
            }
            subtitleWords[i] = words;
        }

        return subtitleWords;


    }

    public static String[][] convertSubtitleUnit(List<String> lines) {
        SubtitleUnit subtitleUnit = new SubtitleUnit(0, null, null, lines);
        String[][] subtitleWords = convertSubtitleUnit(subtitleUnit);
        return subtitleWords;
    }
}
