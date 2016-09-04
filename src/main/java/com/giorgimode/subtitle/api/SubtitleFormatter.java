package com.giorgimode.subtitle.api;

import com.giorgimode.subtitle.exception.InvalidSubtitleLineException;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

/**
 * This class contains utility methods for SubtitleUnit time format related stuff.
 *
 *
 */
public class SubtitleFormatter {
    public static final String TIME_DELIMITER = " --> ";
    public static final String TIME_UNIT_DELIMITER = "[\\s\\:,]+";
    public static final int MS_PER_HOUR = 3600000;
    public static final int MS_PER_MIN = 60000;
    public static final int MS_PER_SEC = 1000;

    public enum Type {
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND
    }
    
    private SubtitleFormatter() {
    }
    
    /**
     * Formats the date into SubtitleUnit time format.
     * @param timestamp the timestamp
     * @return the SubtitleUnit time format
     */
    public static String longToString(long timestamp) throws ParseException {
        SRTTime srtTime = longToSrt(timestamp);
        return srtTime.toString();
    }

    /**
     * Converts long to SRTTime.
     *
     * @param timestamp the timestamp
     * @return the SRTTime
     */
    public static SRTTime longToSrt(long timestamp) {
        long hours = TimeUnit.MILLISECONDS.toHours(timestamp);
        long minutes =  TimeUnit.MILLISECONDS.toMinutes(timestamp) % TimeUnit.HOURS.toMinutes(1);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timestamp) % TimeUnit.MINUTES.toSeconds(1);
        long mls = timestamp % MS_PER_SEC;

        return new SRTTime(
                hours,
                minutes,
                seconds,
                mls);
    }

    /**
     * Parses the SubtitleUnit time format into long timestamp.
     * @param srtTimeString the SubtitleUnit time format
     * @return the long timestamp
     * @throws ParseException
     */
    public static SRTTime stringToSrt(String srtTimeString) throws ParseException {
        String[] times = srtTimeString.split(SubtitleFormatter.TIME_UNIT_DELIMITER);

        long hours = numberSringToLong(times[0]);
        long minutes = numberSringToLong(times[1]);
        long seconds = numberSringToLong(times[2]);
        long milliseconds = numberSringToLong(times[3]);

        return new SRTTime(hours, minutes, seconds, milliseconds);
    }

    /**
     * Converts SRTTime to long.
     *
     * @param srtTime the SRTTime
     * @return the Date
     * @throws ParseException
     */
    public static long srtToLong(SRTTime srtTime) {
        long hours = srtTime.getHour();
        long minutes = srtTime.getMinute();
        long seconds = srtTime.getSecond();
        long mls = srtTime.getMillisecond();

        return hours * MS_PER_HOUR + minutes * MS_PER_MIN + seconds * MS_PER_SEC + mls;
    }

    private static long numberSringToLong(String timeString) {
        long time;
        try {
            timeString = timeString.replaceAll("[^\\d]", "");
            time = Long.parseLong(timeString);
        } catch (NumberFormatException e) {
            throw new InvalidSubtitleLineException(
                    timeString + " has an invalid subtitle number");
        }
        return time;
    }
}
