package org.gio.submaster.editor;

import org.gio.submaster.api.SRTTime;
import org.gio.submaster.api.SubtitleService;
import org.gio.submaster.api.SubtitleFormatter;
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
     * Updates time of the SubtitleUnit object in the SubtitleService object. This method will
     * update both start time and end time.
     * 
     * @param info the SubtitleService object
     * @param subtitleNumber the subtitle number
     * @param type the subtitle time format type
     * @param value the time value
     */
    public static void updateTime(SubtitleService info, int subtitleNumber,
                                  SubtitleFormatter.Type type, int value) {
        if (!info.contains(subtitleNumber)) {
            throw new SubtitleEditorException(subtitleNumber + " could not be found");
        }
        info.add(setTime(info.get(subtitleNumber), type, value));
    }
    
    /**
     * Updates all times of the SubtitleUnit objects in the SubtitleService object. This method will
     * update both start time and end time.
     * 
     * @param info the SubtitleService object
     * @param type the SubtitleUnit time object
     * @param value the time value
     */
    public static void updateTimes(SubtitleService info, SubtitleFormatter.Type type, int value) {
        for (int i = 1; i <= info.size(); i++) {
            updateTime(info, info.get(i).number, type, value);
        }
    }
    
    /**
     * Creates a new SubtitleUnit.
     * 
     * @param subtitleUnit the SubtitleUnit object
     * @param type the SubtitleFormatter
     * @param value the time value
     * @return the new SubtitleUnit object
     */
    static SubtitleUnit setTime(SubtitleUnit subtitleUnit, SubtitleFormatter.Type type, int value) {
        return new SubtitleUnit(subtitleUnit.number, newTime(subtitleUnit.startTime, type, value),
            newTime(subtitleUnit.endTime, type, value), subtitleUnit.text);
    }
    
    private static SRTTime newTime(SRTTime oldTime, SubtitleFormatter.Type type, int value) {
        switch (type) {
        case HOUR:
            oldTime.updateHour(value);
            break;
        case MINUTE:
            oldTime.updateMinute(value);
            break;
        case SECOND:
            oldTime.updateSecond(value);
            break;
        case MILLISECOND:
            oldTime.updateMillisecond(value);
            break;
        }

        return oldTime;
    }

    /**
     * Updates a subtitle text according to the width given.
     * 
     * @param info the SubtitleService object
     * @param subtitleNumber the subtitle number
     * @param width the width (number of characters per subtitle line)
     */
    public static void updateText(SubtitleService info, int subtitleNumber, int width) {
        if (!info.contains(subtitleNumber)) {
            throw new SubtitleEditorException(subtitleNumber + " could not be found");
        }
        info.add(breakText(info.get(subtitleNumber), width));
    }
    
    /**
     * Updates all subtitle texts according to the width given.
     * 
     * @param info the SubtitleService object
     * @param width the width (number of characters per subtitle line)
     */
    public static void updateTexts(SubtitleService info, int width) {
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
     * Appends a new subtitle into SubtitleService.
     * 
     * @param info the SubtitleService object
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void appendSubtitle(SubtitleService info, String startTime,
                                      String endTime, List<String> text) {
        try {
            SubtitleUnit newSubtitleUnit = new SubtitleUnit(
                info.size() + 1,
                SubtitleFormatter.stringToSrt(startTime),
                SubtitleFormatter.stringToSrt(endTime),
                text);
            info.add(newSubtitleUnit);
        } catch (ParseException e) {
            throw new SubtitleEditorException(e);
        }
    }
    
    /**
     * Prepends the subtitle into SubtitleService object.
     * 
     * This operation is very expensive since it needs to update all the subtitle
     * numbers.
     * 
     * @param info the SubtitleService object
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void prependSubtitle(SubtitleService info, String startTime,
                                       String endTime, List<String> text) {
        insertSubtitle(info, 1, startTime, endTime, text);
    }
    
    /**
     * Inserts the subtitle into SubtitleService object. All the subsequent subtitle
     * numbers after the new subtitle that is going to be inserted will be
     * updated.
     * 
     * @param info the SubtitleService object
     * @param subtitleNumber the subtitle number
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void insertSubtitle(SubtitleService info, int subtitleNumber,
                                      String startTime, String endTime, List<String> text) {
        if (!info.contains(subtitleNumber)) {
            throw new SubtitleEditorException(subtitleNumber + " could not be found");
        }
        
        for (int i = info.size(); i >= subtitleNumber; i--) {
            SubtitleUnit tmp = info.get(i);
            info.add(new SubtitleUnit(tmp.number+1, tmp.startTime, tmp.endTime, tmp.text));
        }
        
        try {
            info.add(new SubtitleUnit(subtitleNumber, SubtitleFormatter.stringToSrt(startTime),
                SubtitleFormatter.stringToSrt(endTime), text));
        } catch (ParseException e) {
            throw new SubtitleEditorException(e);
        }
    }
    
    /**
     * Inserts the subtitle into SubtitleService object. All the subsequent subtitle
     * numbers after the new subtitle that is going to be inserted will be
     * updated.
     * 
     * @param info the SubtitleService object
     * @param newSubtitleUnit the new SubtitleUnit
     */
    public static void insertSubtitle(SubtitleService info, SubtitleUnit newSubtitleUnit) {
        for (int i = info.size(); i >= newSubtitleUnit.number; i--) {
            SubtitleUnit tmp = info.get(i);
            info.add(new SubtitleUnit(tmp.number+1, tmp.startTime, tmp.endTime, tmp.text));
        }
        
        info.add(newSubtitleUnit);
    }
    
    /**
     * Removes the subtitle from SubtitleService object. This method will update
     * all the subsequent subtitle numbers after the subtitle number that is
     * going to be removed.
     * 
     * @param info the SubtitleService object
     * @param subtitleNumber the subtitle number to be removed
     */
    public static void removeSubtitle(SubtitleService info, int subtitleNumber) {
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
     * Updates the subtitle from the SubtitleService object.
     * 
     * @param info the SubtitleService object
     * @param subtitleUnit the SubtitleUnit object
     */
    public static void updateSubtitle(SubtitleService info, SubtitleUnit subtitleUnit) {
        info.add(subtitleUnit);
    }
}
