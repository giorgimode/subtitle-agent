package org.gio.submaster.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 *
 */
public class StringUtilsTest {

    @Test
    public void testJoin() {
        assertEquals("Hello World", StringUtils.join(Arrays.asList("Hello", "World"), " "));
        assertEquals(" World", StringUtils.join(Arrays.asList("", "World"), " "));
        assertEquals("Hello ", StringUtils.join(Arrays.asList("Hello", ""), " "));
    }
}
