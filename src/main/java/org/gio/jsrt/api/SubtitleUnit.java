package org.gio.jsrt.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A class to store SubtitleUnit information.
 * 
 *
 */
public class SubtitleUnit implements Comparable<SubtitleUnit> {
    public final int number;
    public final Date startTime;
    public final Date endTime;
    public final List<String> text;
    
    /**
     * Creates a new instance of SubtitleUnit.
     * 
     * @param number the subtitle number
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public SubtitleUnit(int number, Date startTime, Date endTime, String... text) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = new ArrayList<>(Arrays.asList(text));
    }
    
    /**
     * Creates a new instance of SubtitleUnit.
     * 
     * @param number the subtitle number
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public SubtitleUnit(int number, Date startTime, Date endTime, List<String> text) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = new ArrayList<>(text);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (number ^ (number >>> 32));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubtitleUnit other = (SubtitleUnit) obj;
        if (number != other.number)
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(SubtitleUnit o) {
        if (o.startTime != null) {
            return  startTime.compareTo(o.startTime);
        }
        if (o.number > 0 ) {
            return  new Integer(number).compareTo(o.number);
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SubtitleUnit [number=").append(number).append(", startTime=")
            .append(startTime).append(", endTime=").append(endTime).append(", text=")
            .append(text).append("]");
        return builder.toString();
    }
}
