package org.gio.submaster.editor;

import org.gio.submaster.api.SRTTime;
import org.gio.submaster.api.SubtitleData;
import org.gio.submaster.api.SubtitleTimeFormat;
import org.gio.submaster.api.SubtitleUnit;
import org.gio.submaster.exception.SubtitleEditorException;
import org.gio.submaster.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class performs high-level operations related to editing SubtitleUnit.
 * 
 *
 */
public class SubtitleEditor {
    private SubtitleEditor() {
    }
    
    /**
     * Updates time of the SubtitleUnit object in the SubtitleData object. This method will
     * update both start time and end time.
     * 
     * @param info the SubtitleData object
     * @param subtitleNumber the subtitle number
     * @param type the subtitle time format type
     * @param value the time value
     */
    public static void updateTime(SubtitleData info, int subtitleNumber,
                                  SubtitleTimeFormat.Type type, int value) {
        if (!info.contains(subtitleNumber)) {
            throw new SubtitleEditorException(subtitleNumber + " could not be found");
        }
        info.add(setTime(info.get(subtitleNumber), type, value));
    }
    
    /**
     * Updates all times of the SubtitleUnit objects in the SubtitleData object. This method will
     * update both start time and end time.
     * 
     * @param info the SubtitleData object
     * @param type the SubtitleUnit time format object
     * @param value the time value
     */
    public static void updateTimes(SubtitleData info, SubtitleTimeFormat.Type type, int value) {
        for (int i = 1; i <= info.size(); i++) {
            updateTime(info, info.get(i).number, type, value);
        }
    }
    
    /**
     * Creates a new SubtitleUnit.
     * 
     * @param subtitleUnit the SubtitleUnit object
     * @param type the SubtitleTimeFormat
     * @param value the time value
     * @return the new SubtitleUnit object
     */
    static SubtitleUnit setTime(SubtitleUnit subtitleUnit, SubtitleTimeFormat.Type type, int value) {
        return new SubtitleUnit(subtitleUnit.number, newTime(subtitleUnit.startTime, type, value),
            newTime(subtitleUnit.endTime, type, value), subtitleUnit.text);
    }
    
    private static SRTTime newTime(SRTTime oldTime, SubtitleTimeFormat.Type type, int value) {
        switch (type) {
        case HOUR:
            oldTime.setHour(value);
            break;
        case MINUTE:
            oldTime.setMinute(value);
            break;
        case SECOND:
            oldTime.setSecond(value);
            break;
        case MILLISECOND:
            oldTime.setMillisecond(value);
            break;
        }

        return oldTime;
    }

    /**
     * Updates a subtitle text according to the width given.
     * 
     * @param info the SubtitleData object
     * @param subtitleNumber the subtitle number
     * @param width the width (number of characters per subtitle line)
     */
    public static void updateText(SubtitleData info, int subtitleNumber, int width) {
        if (!info.contains(subtitleNumber)) {
            throw new SubtitleEditorException(subtitleNumber + " could not be found");
        }
        info.add(breakText(info.get(subtitleNumber), width));
    }
    
    /**
     * Updates all subtitle texts according to the width given.
     * 
     * @param info the SubtitleData object
     * @param width the width (number of characters per subtitle line)
     */
    public static void updateTexts(SubtitleData info, int width) {
        for (int i = 1; i <= info.size(); i++) {
            updateText(info, info.get(i).number, width);
        }
    }
    
    /**
     * Breaks the subtitle according to the width.
     * 
     * @param subtitleUnit the SubtitleUnit object
     * @param width the width (number of characters per subtitle line)
     * @return the new SubtitleUnit
     */
    static SubtitleUnit breakText(SubtitleUnit subtitleUnit, int width) {
        List<String> newTexts = new ArrayList<>();
        String subtitle = StringUtils.join(subtitleUnit.text, " ");
        if (subtitle.length() <= width) {
            newTexts.addAll(subtitleUnit.text);
        } else {
            int begin = 0;
            int end = width;
            while (end < subtitle.length()) {
                while (subtitle.charAt(end) != ' ' && end != 0) {
                    end--;
                }
                if (end == 0) {
                    break;
                }
                newTexts.add(subtitle.substring(begin, end));
                begin = end + 1;
                end = begin + width;
            }
            newTexts.add(subtitle.substring(begin, subtitle.length()));
        }
        return new SubtitleUnit(subtitleUnit.number, subtitleUnit.startTime, subtitleUnit.endTime, newTexts);
    }
    
    /**
     * Appends a new subtitle into SubtitleData.
     * 
     * @param info the SubtitleData object
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void appendSubtitle(SubtitleData info, String startTime,
                                      String endTime, List<String> text) {
        try {
            SubtitleUnit newSubtitleUnit = new SubtitleUnit(
                info.size() + 1,
                SubtitleTimeFormat.parse(startTime),
                SubtitleTimeFormat.parse(endTime),
                text);
            info.add(newSubtitleUnit);
        } catch (ParseException e) {
            throw new SubtitleEditorException(e);
        }
    }
    
    /**
     * Prepends the subtitle into SubtitleData object.
     * 
     * This operation is very expensive since it needs to update all the subtitle
     * numbers.
     * 
     * @param info the SubtitleData object
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void prependSubtitle(SubtitleData info, String startTime,
                                       String endTime, List<String> text) {
        insertSubtitle(info, 1, startTime, endTime, text);
    }
    
    /**
     * Inserts the subtitle into SubtitleData object. All the subsequent subtitle
     * numbers after the new subtitle that is going to be inserted will be
     * updated.
     * 
     * @param info the SubtitleData object
     * @param subtitleNumber the subtitle number
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void insertSubtitle(SubtitleData info, int subtitleNumber,
                                      String startTime, String endTime, List<String> text) {
        if (!info.contains(subtitleNumber)) {
            throw new SubtitleEditorException(subtitleNumber + " could not be found");
        }
        
        for (int i = info.size(); i >= subtitleNumber; i--) {
            SubtitleUnit tmp = info.get(i);
            info.add(new SubtitleUnit(tmp.number+1, tmp.startTime, tmp.endTime, tmp.text));
        }
        
        try {
            info.add(new SubtitleUnit(subtitleNumber, SubtitleTimeFormat.parse(startTime),
                SubtitleTimeFormat.parse(endTime), text));
        } catch (ParseException e) {
            throw new SubtitleEditorException(e);
        }
    }
    
    /**
     * Inserts the subtitle into SubtitleData object. All the subsequent subtitle
     * numbers after the new subtitle that is going to be inserted will be
     * updated.
     * 
     * @param info the SubtitleData object
     * @param newSubtitleUnit the new SubtitleUnit
     */
    public static void insertSubtitle(SubtitleData info, SubtitleUnit newSubtitleUnit) {
        for (int i = info.size(); i >= newSubtitleUnit.number; i--) {
            SubtitleUnit tmp = info.get(i);
            info.add(new SubtitleUnit(tmp.number+1, tmp.startTime, tmp.endTime, tmp.text));
        }
        
        info.add(newSubtitleUnit);
    }
    
    /**
     * Removes the subtitle from SubtitleData object. This method will update
     * all the subsequent subtitle numbers after the subtitle number that is
     * going to be removed.
     * 
     * @param info the SubtitleData object
     * @param subtitleNumber the subtitle number to be removed
     */
    public static void removeSubtitle(SubtitleData info, int subtitleNumber) {
        if (!info.contains(subtitleNumber)) {
            throw new SubtitleEditorException(subtitleNumber + " could not be found");
        }
        
        int originalSize = info.size();
        for (int i = subtitleNumber+1; i  <= originalSize; i++) {
            SubtitleUnit tmp = info.get(i);
            info.add(new SubtitleUnit(tmp.number-1, tmp.startTime, tmp.endTime, tmp.text));
        }
        // Remove the last element
        info.remove(info.size());
    }
    
    /**
     * Updates the subtitle from the SubtitleData object.
     * 
     * @param info the SubtitleData object
     * @param subtitleUnit the SubtitleUnit object
     */
    public static void updateSubtitle(SubtitleData info, SubtitleUnit subtitleUnit) {
        info.add(subtitleUnit);
    }
}
