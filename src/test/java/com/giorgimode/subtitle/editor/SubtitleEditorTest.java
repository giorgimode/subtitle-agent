package com.giorgimode.subtitle.editor;

import com.giorgimode.subtitle.util.StringUtils;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subtitle.api.SubtitleFormatter;
import com.giorgimode.subtitle.api.SubtitleUnit;
import com.giorgimode.subtitle.exception.SubtitleEditorException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@Ignore
public class SubtitleEditorTest {

    @Test
    public void testSetTime() throws Exception {
        SubtitleUnit oldSubtitleUnit = new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Hello World");
        
        SubtitleUnit newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.HOUR, 13);
        assertEquals(oldSubtitleUnit.number, newSubtitleUnit.number);
        assertEquals("13:00:24,600", newSubtitleUnit.startTime.toString());
        assertEquals("13:00:26,600", newSubtitleUnit.endTime.toString());
        assertEquals(oldSubtitleUnit.text.get(0), newSubtitleUnit.text.get(0));
        
        newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.MINUTE, 2);
        assertEquals(oldSubtitleUnit.number, newSubtitleUnit.number);
        assertEquals("00:02:24,600", newSubtitleUnit.startTime.toString());
        assertEquals("00:02:26,600", newSubtitleUnit.endTime.toString());
        assertEquals(oldSubtitleUnit.text.get(0), newSubtitleUnit.text.get(0));
        
        newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.SECOND, 2);
        assertEquals(oldSubtitleUnit.number, newSubtitleUnit.number);
        assertEquals("00:00:26,600", newSubtitleUnit.startTime.toString());
        assertEquals("00:00:28,600", newSubtitleUnit.endTime.toString());
        assertEquals(oldSubtitleUnit.text.get(0), newSubtitleUnit.text.get(0));
        
        newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.MILLISECOND, -200);
        assertEquals(oldSubtitleUnit.number, newSubtitleUnit.number);
        assertEquals("00:00:24,400", newSubtitleUnit.startTime.toString());
        assertEquals("00:00:26,400", newSubtitleUnit.endTime.toString());
        assertEquals(oldSubtitleUnit.text.get(0), newSubtitleUnit.text.get(0));
    }
    
    @Test
    public void testUpdateTime() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        subtitleService.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo", "Bar"));
        subtitleService.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"), "Bye", "World"));
        
        SubtitleEditor.updateTime(subtitleService, 2, SubtitleFormatter.Type.MINUTE, -2);
        SubtitleUnit subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.number);
        assertEquals("00:09:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:10:26,600", subtitleUnit.endTime.toString());
        assertEquals("Bye", subtitleUnit.text.get(0));
        assertEquals("World", subtitleUnit.text.get(1));
    }
    
    @Test(expected = SubtitleEditorException.class)
    public void testUpdateTimeInvalidSubtitleNumber() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        subtitleService.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo", "Bar"));
        subtitleService.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"), "Bye", "World"));
        
        SubtitleEditor.updateTime(subtitleService, 100, SubtitleFormatter.Type.MINUTE, -2);
    }
    
    @Test
    public void testUpdateTimes() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        subtitleService.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo", "Bar"));
        subtitleService.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"), "Bye", "World"));
        
        SubtitleEditor.updateTimes(subtitleService, SubtitleFormatter.Type.MINUTE, -2);
        
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals(1L, subtitleUnit.number);
        assertEquals("23:58:24,600", subtitleUnit.startTime.toString());
        assertEquals("23:58:26,600", subtitleUnit.endTime.toString());
        assertEquals("Foo", subtitleUnit.text.get(0));
        assertEquals("Bar", subtitleUnit.text.get(1));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.number);
        assertEquals("00:09:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:10:26,600", subtitleUnit.endTime.toString());
        assertEquals("Bye", subtitleUnit.text.get(0));
        assertEquals("World", subtitleUnit.text.get(1));
    }
    
    @Test
    public void testBreakText() {
        SubtitleUnit oldSubtitleUnit = new SubtitleUnit(1, null, null, "0123456789 0123456789 0123456789 0123456789");
        SubtitleUnit newSubtitleUnit = SubtitleEditor.breakText(oldSubtitleUnit, 21);
        assertEquals(oldSubtitleUnit.number, newSubtitleUnit.number);
        assertEquals(oldSubtitleUnit.startTime.toString(), newSubtitleUnit.startTime.toString());
        assertEquals(oldSubtitleUnit.endTime.toString(), newSubtitleUnit.endTime.toString());
        assertEquals(2, newSubtitleUnit.text.size());
        assertEquals("0123456789 0123456789", newSubtitleUnit.text.get(0));
        assertEquals("0123456789 0123456789", newSubtitleUnit.text.get(1));
        
        oldSubtitleUnit = new SubtitleUnit(1, null, null, "0123456789 0123456789 0123456789 0123456789 0123456789");
        newSubtitleUnit = SubtitleEditor.breakText(oldSubtitleUnit, 21);
        assertEquals(oldSubtitleUnit.number, newSubtitleUnit.number);
        assertEquals(oldSubtitleUnit.startTime.toString(), newSubtitleUnit.startTime.toString());
        assertEquals(oldSubtitleUnit.endTime.toString(), newSubtitleUnit.endTime.toString());
        assertEquals(3, newSubtitleUnit.text.size());
        assertEquals("0123456789 0123456789", newSubtitleUnit.text.get(0));
        assertEquals("0123456789 0123456789", newSubtitleUnit.text.get(1));
        assertEquals("0123456789", newSubtitleUnit.text.get(2));
        
        oldSubtitleUnit = new SubtitleUnit(1, null, null, "0123456789");
        newSubtitleUnit = SubtitleEditor.breakText(oldSubtitleUnit, 5);
        assertEquals(oldSubtitleUnit.number, newSubtitleUnit.number);
        assertEquals(oldSubtitleUnit.startTime.toString(), newSubtitleUnit.startTime.toString());
        assertEquals(oldSubtitleUnit.endTime.toString(), newSubtitleUnit.endTime.toString());
        assertEquals(1, newSubtitleUnit.text.size());
        assertEquals("0123456789", newSubtitleUnit.text.get(0));
    }
    
    @Test
    public void testUpdateText() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        subtitleService.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        subtitleService.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SubtitleEditor.updateText(subtitleService, 2, 15);
        
        SubtitleUnit subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.number);
        assertEquals("00:11:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:12:26,600", subtitleUnit.endTime.toString());
        assertEquals(5, subtitleUnit.text.size());
        assertEquals("Hello!!! There", subtitleUnit.text.get(0));
        assertEquals("is really", subtitleUnit.text.get(1));
        assertEquals("nothing to see", subtitleUnit.text.get(2));
        assertEquals("here. Foo Bar.", subtitleUnit.text.get(3));
        assertEquals("Bye World.", subtitleUnit.text.get(4));
    }
    
    @Test(expected = SubtitleEditorException.class)
    public void testUpdateTextInvalidSubtitleNumber() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        subtitleService.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        subtitleService.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SubtitleEditor.updateText(subtitleService, 100, 15);
    }
    
    @Test
    public void testUpdateTexts() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        subtitleService.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        subtitleService.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SubtitleEditor.updateTexts(subtitleService, 15);
        
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals(1L, subtitleUnit.number);
        assertEquals("00:00:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:00:26,600", subtitleUnit.endTime.toString());
        assertEquals(4, subtitleUnit.text.size());
        assertEquals("Hello!!! This", subtitleUnit.text.get(0));
        assertEquals("is a very long", subtitleUnit.text.get(1));
        assertEquals("string. Ain't", subtitleUnit.text.get(2));
        assertEquals("it cool??? :)", subtitleUnit.text.get(3));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.number);
        assertEquals("00:11:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:12:26,600", subtitleUnit.endTime.toString());
        assertEquals(5, subtitleUnit.text.size());
        assertEquals("Hello!!! There", subtitleUnit.text.get(0));
        assertEquals("is really", subtitleUnit.text.get(1));
        assertEquals("nothing to see", subtitleUnit.text.get(2));
        assertEquals("here. Foo Bar.", subtitleUnit.text.get(3));
        assertEquals("Bye World.", subtitleUnit.text.get(4));
    }
    
    @Test
    public void testAppendSubtitle() {
        SubtitleService subtitleService = new SubtitleService();
        SubtitleEditor.appendSubtitle(subtitleService, "00:00:24,600", "00:00:26,600", Arrays.asList("Foo"));
        SubtitleEditor.appendSubtitle(subtitleService, "00:11:24,600", "00:12:26,600", Arrays.asList("Bar"));
        
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals(1L, subtitleUnit.number);
        assertEquals("00:00:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:00:26,600", subtitleUnit.endTime.toString());
        assertEquals(1, subtitleUnit.text.size());
        assertEquals("Foo", subtitleUnit.text.get(0));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.number);
        assertEquals("00:11:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:12:26,600", subtitleUnit.endTime.toString());
        assertEquals(1, subtitleUnit.text.size());
    }
    
    @Test
    public void testRemoveSubtitle() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        for (int i = 1; i <= 5; i++) {
            subtitleService.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.removeSubtitle(subtitleService, 3);
        
        assertEquals(4, subtitleService.size());
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        Assert.assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo4", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo5", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test(expected = SubtitleEditorException.class)
    public void testRemoveSubtitleInvalidSubtitleNumber() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        for (int i = 1; i <= 5; i++) {
            subtitleService.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.removeSubtitle(subtitleService, 100);
    }
    
    @Test
    public void testInsertSubtitle1() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        for (int i = 1; i <= 4; i++) {
            subtitleService.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.insertSubtitle(subtitleService, 3, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
        
        assertEquals(5, subtitleService.size());
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo100", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo3", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(5);
        assertEquals("Foo4", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test
    public void testInsertSubtitle2() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        for (int i = 1; i <= 4; i++) {
            subtitleService.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.insertSubtitle(subtitleService,
            new SubtitleUnit(3,
            SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            Arrays.asList("Foo100")));
        
        assertEquals(5, subtitleService.size());
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo100", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo3", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(5);
        assertEquals("Foo4", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test(expected = SubtitleEditorException.class)
    public void testInsertSubtitleInvalidSubtitleNumber() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        for (int i = 1; i <= 4; i++) {
            subtitleService.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.insertSubtitle(subtitleService, 100, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
    }
    
    @Test
    public void testPrepend() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        for (int i = 1; i <= 3; i++) {
            subtitleService.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.prependSubtitle(subtitleService, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
        
        assertEquals(4, subtitleService.size());
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals("Foo100", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo3", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test
    public void testUpdateSubtitle() throws Exception {
        SubtitleService subtitleService = new SubtitleService();
        for (int i = 1; i <= 4; i++) {
            subtitleService.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.updateSubtitle(subtitleService,
            new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "test"));
        
        SubtitleUnit s = subtitleService.get(2);
        assertEquals("test", StringUtils.join(s.text, ""));
    }
}
