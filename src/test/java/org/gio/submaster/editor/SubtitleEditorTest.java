package org.gio.submaster.editor;

import org.gio.submaster.api.SubtitleData;
import org.gio.submaster.api.SubtitleFormatter;
import org.gio.submaster.api.SubtitleUnit;
import org.gio.submaster.exception.SubtitleEditorException;
import org.gio.submaster.util.StringUtils;
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
        SubtitleData subtitleData = new SubtitleData();
        subtitleData.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo", "Bar"));
        subtitleData.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"), "Bye", "World"));
        
        SubtitleEditor.updateTime(subtitleData, 2, SubtitleFormatter.Type.MINUTE, -2);
        SubtitleUnit subtitleUnit = subtitleData.get(2);
        assertEquals(2L, subtitleUnit.number);
        assertEquals("00:09:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:10:26,600", subtitleUnit.endTime.toString());
        assertEquals("Bye", subtitleUnit.text.get(0));
        assertEquals("World", subtitleUnit.text.get(1));
    }
    
    @Test(expected = SubtitleEditorException.class)
    public void testUpdateTimeInvalidSubtitleNumber() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        subtitleData.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo", "Bar"));
        subtitleData.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"), "Bye", "World"));
        
        SubtitleEditor.updateTime(subtitleData, 100, SubtitleFormatter.Type.MINUTE, -2);
    }
    
    @Test
    public void testUpdateTimes() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        subtitleData.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo", "Bar"));
        subtitleData.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"), "Bye", "World"));
        
        SubtitleEditor.updateTimes(subtitleData, SubtitleFormatter.Type.MINUTE, -2);
        
        SubtitleUnit subtitleUnit = subtitleData.get(1);
        assertEquals(1L, subtitleUnit.number);
        assertEquals("23:58:24,600", subtitleUnit.startTime.toString());
        assertEquals("23:58:26,600", subtitleUnit.endTime.toString());
        assertEquals("Foo", subtitleUnit.text.get(0));
        assertEquals("Bar", subtitleUnit.text.get(1));
        
        subtitleUnit = subtitleData.get(2);
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
        SubtitleData subtitleData = new SubtitleData();
        subtitleData.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        subtitleData.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SubtitleEditor.updateText(subtitleData, 2, 15);
        
        SubtitleUnit subtitleUnit = subtitleData.get(2);
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
        SubtitleData subtitleData = new SubtitleData();
        subtitleData.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        subtitleData.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SubtitleEditor.updateText(subtitleData, 100, 15);
    }
    
    @Test
    public void testUpdateTexts() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        subtitleData.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        subtitleData.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:11:24,600"),
            SubtitleFormatter.stringToSrt("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SubtitleEditor.updateTexts(subtitleData, 15);
        
        SubtitleUnit subtitleUnit = subtitleData.get(1);
        assertEquals(1L, subtitleUnit.number);
        assertEquals("00:00:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:00:26,600", subtitleUnit.endTime.toString());
        assertEquals(4, subtitleUnit.text.size());
        assertEquals("Hello!!! This", subtitleUnit.text.get(0));
        assertEquals("is a very long", subtitleUnit.text.get(1));
        assertEquals("string. Ain't", subtitleUnit.text.get(2));
        assertEquals("it cool??? :)", subtitleUnit.text.get(3));
        
        subtitleUnit = subtitleData.get(2);
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
        SubtitleData subtitleData = new SubtitleData();
        SubtitleEditor.appendSubtitle(subtitleData, "00:00:24,600", "00:00:26,600", Arrays.asList("Foo"));
        SubtitleEditor.appendSubtitle(subtitleData, "00:11:24,600", "00:12:26,600", Arrays.asList("Bar"));
        
        SubtitleUnit subtitleUnit = subtitleData.get(1);
        assertEquals(1L, subtitleUnit.number);
        assertEquals("00:00:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:00:26,600", subtitleUnit.endTime.toString());
        assertEquals(1, subtitleUnit.text.size());
        assertEquals("Foo", subtitleUnit.text.get(0));
        
        subtitleUnit = subtitleData.get(2);
        assertEquals(2L, subtitleUnit.number);
        assertEquals("00:11:24,600", subtitleUnit.startTime.toString());
        assertEquals("00:12:26,600", subtitleUnit.endTime.toString());
        assertEquals(1, subtitleUnit.text.size());
    }
    
    @Test
    public void testRemoveSubtitle() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        for (int i = 1; i <= 5; i++) {
            subtitleData.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.removeSubtitle(subtitleData, 3);
        
        assertEquals(4, subtitleData.size());
        SubtitleUnit subtitleUnit = subtitleData.get(1);
        assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(2);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(3);
        assertEquals("Foo4", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(4);
        assertEquals("Foo5", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test(expected = SubtitleEditorException.class)
    public void testRemoveSubtitleInvalidSubtitleNumber() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        for (int i = 1; i <= 5; i++) {
            subtitleData.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.removeSubtitle(subtitleData, 100);
    }
    
    @Test
    public void testInsertSubtitle1() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        for (int i = 1; i <= 4; i++) {
            subtitleData.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.insertSubtitle(subtitleData, 3, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
        
        assertEquals(5, subtitleData.size());
        SubtitleUnit subtitleUnit = subtitleData.get(1);
        assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(2);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(3);
        assertEquals("Foo100", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(4);
        assertEquals("Foo3", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(5);
        assertEquals("Foo4", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test
    public void testInsertSubtitle2() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        for (int i = 1; i <= 4; i++) {
            subtitleData.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.insertSubtitle(subtitleData,
            new SubtitleUnit(3,
            SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"),
            Arrays.asList("Foo100")));
        
        assertEquals(5, subtitleData.size());
        SubtitleUnit subtitleUnit = subtitleData.get(1);
        assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(2);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(3);
        assertEquals("Foo100", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(4);
        assertEquals("Foo3", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(5);
        assertEquals("Foo4", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test(expected = SubtitleEditorException.class)
    public void testInsertSubtitleInvalidSubtitleNumber() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        for (int i = 1; i <= 4; i++) {
            subtitleData.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.insertSubtitle(subtitleData, 100, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
    }
    
    @Test
    public void testPrepend() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        for (int i = 1; i <= 3; i++) {
            subtitleData.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.prependSubtitle(subtitleData, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
        
        assertEquals(4, subtitleData.size());
        SubtitleUnit subtitleUnit = subtitleData.get(1);
        assertEquals("Foo100", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(2);
        assertEquals("Foo1", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(3);
        assertEquals("Foo2", StringUtils.join(subtitleUnit.text, ""));
        
        subtitleUnit = subtitleData.get(4);
        assertEquals("Foo3", StringUtils.join(subtitleUnit.text, ""));
    }
    
    @Test
    public void testUpdateSubtitle() throws Exception {
        SubtitleData subtitleData = new SubtitleData();
        for (int i = 1; i <= 4; i++) {
            subtitleData.add(new SubtitleUnit(i, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "Foo" + i));
        }
        SubtitleEditor.updateSubtitle(subtitleData,
            new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:26,600"), "test"));
        
        SubtitleUnit s = subtitleData.get(2);
        assertEquals("test", StringUtils.join(s.text, ""));
    }
}
