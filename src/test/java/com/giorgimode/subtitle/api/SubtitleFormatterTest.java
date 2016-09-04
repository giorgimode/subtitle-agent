package com.giorgimode.subtitle.api;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by modeg on 8/28/2016.
 */
public class SubtitleFormatterTest {
    @Test
    public void formatTest() throws Exception {
        String srtTime = SubtitleFormatter.longToString(446336L);
        assertEquals("00:07:26,336", srtTime.toString());
    }

    @Test
    public void parseTest() throws Exception {
        SRTTime srtTime = SubtitleFormatter.stringToSrt("01:20:45,558");
        assertEquals(1, srtTime.getHour());
        assertEquals(20, srtTime.getMinute());
        assertEquals(45, srtTime.getSecond());
        assertEquals(558, srtTime.getMillisecond());
    }

    @Test
    public void toSRTTimeTest() throws Exception {
        SRTTime srtTime = SubtitleFormatter.longToSrt(4845558L);
        assertEquals(1, srtTime.getHour());
        assertEquals(20, srtTime.getMinute());
        assertEquals(45, srtTime.getSecond());
        assertEquals(558, srtTime.getMillisecond());
    }

    @Test
    public void fromSRTTimeTest() throws Exception {
        SRTTime srtTime = SubtitleFormatter.stringToSrt("01:20:45,558");
        long timeStamp = SubtitleFormatter.srtToLong(srtTime);
        assertEquals(4845558L, timeStamp);
    }

}