package com.giorgimode.subtitle.util;

import com.giorgimode.subtitle.api.SubtitleUnit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class StringUtilsTest {

    @Test
    public void testJoin() {
        assertEquals("Hello World", SubtitleUtils.join(Arrays.asList("Hello", "World"), " "));
        assertEquals(" World", SubtitleUtils.join(Arrays.asList("", "World"), " "));
        assertEquals("Hello ", SubtitleUtils.join(Arrays.asList("Hello", ""), " "));
    }

    @Test
   public void testConvertSubtitleUnit(){
        List<String> lines = new ArrayList<>();
        lines.add("We've unlocked <b>some</b> secrets");
        lines.add("of the deep-rooted subconscious.");
        SubtitleUnit subtitleUnit = new SubtitleUnit(0, null, null, lines);

        String[][] words = SubtitleUtils.convertSubtitleUnit(subtitleUnit);
        String[] line1 = words[0];
        assertEquals("We've", line1[0]);
        assertEquals("unlocked", line1[1]);
        assertEquals("some", line1[2]);
        assertEquals("secrets", line1[3]);

        String[] line2 = words[1];
        assertEquals("of", line2[0]);
        assertEquals("the", line2[1]);
        assertEquals("deep-rooted", line2[2]);
        assertEquals("subconscious", line2[3]);
    }
}
