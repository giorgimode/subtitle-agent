package org.gio.jsrt.api;

import org.gio.jsrt.exception.InvalidSubtitleLineException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class contains utility methods for SubtitleUnit time format related stuff.
 * 
 *
 */
public class SubtitleTimeFormat {
    public static final String TIME_DELIMITER = " --> ";
    public static final String TIME_UNIT_DELIMITER = "[\\s\\:,]+";
    public static final String TIME_FORMAT = "HH:mm:ss,SSS";
    public static final String HOUR_FORMAT = "HH";
    public static final String MINUTE_FORMAT = "mm";
    public static final String SECOND_FORMAT = "ss";
    public static final String MILLISECOND_FORMAT = "SSS";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
    
    public enum Type {
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND
    }
    
    private SubtitleTimeFormat() {
    }
    
    /**
     * Formats the date into SubtitleUnit time format.
     * @param date the date
     * @return the SubtitleUnit time format
     */
    public static String format(long date) throws ParseException {
        SRTTime srtTime = toSRTTime(date);
//TODO        return sdf.format(srtTime.toString());
        return sdf.format(fromSRTTimeToDate(srtTime));
    }

    /**
     * Parses the SubtitleUnit time format into long timestamp.
     * @param srtTimeString the SubtitleUnit time format
     * @return the long timestamp
     * @throws ParseException
     */
    public static long parse(String srtTimeString) throws ParseException {
        String[] times = srtTimeString.split(SubtitleTimeFormat.TIME_UNIT_DELIMITER);

        long hours = timestringToLong(times[0]);
        long minutes = timestringToLong(times[1]);
        long seconds = timestringToLong(times[2]);
        long milliseconds = timestringToLong(times[3]);

        SRTTime srtTime = new SRTTime(hours, minutes, seconds, milliseconds);
        return fromSRTTime(srtTime);
    }

    private static long timestringToLong(String timeString) {
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

    /**
     * Converts long to SRTTime.
     * 
     * @param timestamp the timestamp
     * @return the SRTTime
     */
    public static SRTTime toSRTTime(long timestamp) {
        long hours = TimeUnit.MILLISECONDS.toHours(timestamp);
        long minutes =  TimeUnit.MILLISECONDS.toMinutes(timestamp) % TimeUnit.HOURS.toMinutes(1);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timestamp) % TimeUnit.MINUTES.toSeconds(1);
        long mls = timestamp % 1000;

        return new SRTTime(
                hours,
                minutes,
                seconds,
                mls);
    }

    /**
     * Converts SRTTime to Date.
     *
     * @param srtTime the SRTTime
     * @return the Date
     * @throws ParseException
     */
    public static Date fromSRTTimeToDate(SRTTime srtTime) throws ParseException {
        return sdf.parse(srtTime.toString());
    }

    /**
     * Converts SRTTime to long.
     * 
     * @param srtTime the SRTTime
     * @return the Date
     * @throws ParseException
     */
    public static long fromSRTTime(SRTTime srtTime) {
        long hours = srtTime.getHour();
        long minutes = srtTime.getMinute();
        long seconds = srtTime.getSecond();
        long mls = srtTime.getMillisecond();

        return hours * 3600000 + minutes * 60000 + seconds * 1000 + mls;
    }
}
