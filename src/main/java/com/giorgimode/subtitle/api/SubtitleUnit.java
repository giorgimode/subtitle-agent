package com.giorgimode.subtitle.api;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class to store SubtitleUnit information.
 */
@Getter
public class SubtitleUnit implements Comparable<SubtitleUnit> {
    private final int          number;
    private final SRTTime      startTime;
    private final SRTTime      endTime;
    private final List<String> text;

    /**
     * Creates a new instance of SubtitleUnit.
     *
     * @param number    the subtitle number
     * @param startTime the start time
     * @param endTime   the end time
     * @param text      the subtitle text
     */
    public SubtitleUnit(int number, SRTTime startTime, SRTTime endTime, String... text) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = new ArrayList<>(Arrays.asList(text));
    }

    /**
     * Creates a new instance of SubtitleUnit.
     *
     * @param number    the subtitle number
     * @param startTime the start time
     * @param endTime   the end time
     * @param text      the subtitle text
     */
    public SubtitleUnit(int number, SRTTime startTime, SRTTime endTime, List<String> text) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = new ArrayList<>(text);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getNumber() ^ (getNumber() >>> 16));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SubtitleUnit other = (SubtitleUnit) obj;
        return getNumber() == other.getNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(SubtitleUnit o) {
        if (getStartTime() != null && o.getStartTime() != null) {
            return getStartTime().compareTo(o.getStartTime());
        }

        return new Integer(getNumber()).compareTo(o.getNumber());
    }

    @Override
    public String toString() {
        return "SubtitleUnit [number=" + getNumber() + ", startTime="
               + getStartTime() + ", endTime=" + getEndTime() + ", text="
               + getText() + "]";
    }
}
