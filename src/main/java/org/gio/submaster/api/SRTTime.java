package org.gio.submaster.api;

public class SRTTime implements Comparable<SRTTime>{
        private long hour;
        private long minute;
        private long second;
        private long millisecond;

        public SRTTime(long hour, long minute, long second, long millisecond) {
            this.setHour(hour);
            this.setMinute(minute);
            this.setSecond(second);
            this.setMillisecond(millisecond);
        }

    public long getHour() {
        return hour;
    }

    public long getMinute() {
        return minute;
    }

    public long getSecond() {
        return second;
    }

    public long getMillisecond() {
        return millisecond;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public void setMillisecond(long millisecond) {
        this.millisecond = millisecond;
    }

    @Override
    public String toString() {
        StringBuilder timeStr = new StringBuilder();
        if (getHour() < 10) {
            timeStr.append("0");
        }
        timeStr.append(Long.toString(getHour()));
        timeStr.append(":");
        if (getMinute() < 10) {
            timeStr.append("0");
        }
        timeStr.append(Long.toString(getMinute()));
        timeStr.append(":");
        if (getSecond() < 10) {
            timeStr.append("0");
        }
        timeStr.append(Long.toString(getSecond()));
        timeStr.append(",");
        if (getMillisecond() < 10) {
            timeStr.append("00");
        } else if (getMillisecond() < 100) {
            timeStr.append("0");
        }
        timeStr.append(Long.toString(getMillisecond()));
        return timeStr.toString();
    }

    @Override
    public int compareTo(SRTTime o){
        if (getHour() > o.getHour()) return 1;
        else if (getHour() < o.getHour()) return -1;
        else {
            if (getMinute() > o.getMinute()) return 1;
            else if (getMinute() < o.getMinute()) return -1;
            else {
                if (getSecond() > o.getSecond()) return 1;
                else if (getSecond() < o.getSecond()) return -1;
                else {
                    if (getMillisecond() > o.getMillisecond()) return 1;
                    else if (getMillisecond() < o.getMillisecond()) return -1;
                    else return 0;
                }
            }
        }
    }
}