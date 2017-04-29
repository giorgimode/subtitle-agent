package com.giorgimode.subtitle.editor;

import com.giorgimode.subtitle.util.SubtitleUtils;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subtitle.api.SubtitleFormatter;
import com.giorgimode.subtitle.api.SubtitleUnit;
import com.giorgimode.subtitle.exception.SubtitleEditorException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@Ignore
public class SubtitleEditorTest {

    @Test
    public void testSetTime() throws Exception {
        SubtitleUnit oldSubtitleUnit = new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:24,600"),
            SubtitleFormatter.stringToSrt("00:00:26,600"), "Hello World");
        
        SubtitleUnit newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.HOUR, 13);
        assertEquals(oldSubtitleUnit.getNumber(), newSubtitleUnit.getNumber());
        assertEquals("13:00:24,600", newSubtitleUnit.getStartTime().toString());
        assertEquals("13:00:26,600", newSubtitleUnit.getEndTime().toString());
        assertEquals(oldSubtitleUnit.getText().get(0), newSubtitleUnit.getText().get(0));
        
        newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.MINUTE, 2);
        assertEquals(oldSubtitleUnit.getNumber(), newSubtitleUnit.getNumber());
        assertEquals("00:02:24,600", newSubtitleUnit.getStartTime().toString());
        assertEquals("00:02:26,600", newSubtitleUnit.getEndTime().toString());
        assertEquals(oldSubtitleUnit.getText().get(0), newSubtitleUnit.getText().get(0));
        
        newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.SECOND, 2);
        assertEquals(oldSubtitleUnit.getNumber(), newSubtitleUnit.getNumber());
        assertEquals("00:00:26,600", newSubtitleUnit.getStartTime().toString());
        assertEquals("00:00:28,600", newSubtitleUnit.getEndTime().toString());
        assertEquals(oldSubtitleUnit.getText().get(0), newSubtitleUnit.getText().get(0));
        
        newSubtitleUnit = SubtitleEditor.setTime(oldSubtitleUnit, SubtitleFormatter.Type.MILLISECOND, -200);
        assertEquals(oldSubtitleUnit.getNumber(), newSubtitleUnit.getNumber());
        assertEquals("00:00:24,400", newSubtitleUnit.getStartTime().toString());
        assertEquals("00:00:26,400", newSubtitleUnit.getEndTime().toString());
        assertEquals(oldSubtitleUnit.getText().get(0), newSubtitleUnit.getText().get(0));
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
        assertEquals(2L, subtitleUnit.getNumber());
        assertEquals("00:09:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("00:10:26,600", subtitleUnit.getEndTime().toString());
        assertEquals("Bye", subtitleUnit.getText().get(0));
        assertEquals("World", subtitleUnit.getText().get(1));
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
        assertEquals(1L, subtitleUnit.getNumber());
        assertEquals("23:58:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("23:58:26,600", subtitleUnit.getEndTime().toString());
        assertEquals("Foo", subtitleUnit.getText().get(0));
        assertEquals("Bar", subtitleUnit.getText().get(1));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.getNumber());
        assertEquals("00:09:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("00:10:26,600", subtitleUnit.getEndTime().toString());
        assertEquals("Bye", subtitleUnit.getText().get(0));
        assertEquals("World", subtitleUnit.getText().get(1));
    }
    
    @Test
    public void testBreakText() {
        SubtitleUnit oldSubtitleUnit = new SubtitleUnit(1, null, null, "0123456789 0123456789 0123456789 0123456789");
        SubtitleUnit newSubtitleUnit = SubtitleEditor.breakText(oldSubtitleUnit, 21);
        assertEquals(oldSubtitleUnit.getNumber(), newSubtitleUnit.getNumber());
        assertEquals(oldSubtitleUnit.getStartTime().toString(), newSubtitleUnit.getStartTime().toString());
        assertEquals(oldSubtitleUnit.getEndTime().toString(), newSubtitleUnit.getEndTime().toString());
        assertEquals(2, newSubtitleUnit.getText().size());
        assertEquals("0123456789 0123456789", newSubtitleUnit.getText().get(0));
        assertEquals("0123456789 0123456789", newSubtitleUnit.getText().get(1));
        
        oldSubtitleUnit = new SubtitleUnit(1, null, null, "0123456789 0123456789 0123456789 0123456789 0123456789");
        newSubtitleUnit = SubtitleEditor.breakText(oldSubtitleUnit, 21);
        assertEquals(oldSubtitleUnit.getNumber(), newSubtitleUnit.getNumber());
        assertEquals(oldSubtitleUnit.getStartTime().toString(), newSubtitleUnit.getStartTime().toString());
        assertEquals(oldSubtitleUnit.getEndTime().toString(), newSubtitleUnit.getEndTime().toString());
        assertEquals(3, newSubtitleUnit.getText().size());
        assertEquals("0123456789 0123456789", newSubtitleUnit.getText().get(0));
        assertEquals("0123456789 0123456789", newSubtitleUnit.getText().get(1));
        assertEquals("0123456789", newSubtitleUnit.getText().get(2));
        
        oldSubtitleUnit = new SubtitleUnit(1, null, null, "0123456789");
        newSubtitleUnit = SubtitleEditor.breakText(oldSubtitleUnit, 5);
        assertEquals(oldSubtitleUnit.getNumber(), newSubtitleUnit.getNumber());
        assertEquals(oldSubtitleUnit.getStartTime().toString(), newSubtitleUnit.getStartTime().toString());
        assertEquals(oldSubtitleUnit.getEndTime().toString(), newSubtitleUnit.getEndTime().toString());
        assertEquals(1, newSubtitleUnit.getText().size());
        assertEquals("0123456789", newSubtitleUnit.getText().get(0));
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
        assertEquals(2L, subtitleUnit.getNumber());
        assertEquals("00:11:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("00:12:26,600", subtitleUnit.getEndTime().toString());
        assertEquals(5, subtitleUnit.getText().size());
        assertEquals("Hello!!! There", subtitleUnit.getText().get(0));
        assertEquals("is really", subtitleUnit.getText().get(1));
        assertEquals("nothing to see", subtitleUnit.getText().get(2));
        assertEquals("here. Foo Bar.", subtitleUnit.getText().get(3));
        assertEquals("Bye World.", subtitleUnit.getText().get(4));
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
        assertEquals(1L, subtitleUnit.getNumber());
        assertEquals("00:00:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("00:00:26,600", subtitleUnit.getEndTime().toString());
        assertEquals(4, subtitleUnit.getText().size());
        assertEquals("Hello!!! This", subtitleUnit.getText().get(0));
        assertEquals("is a very long", subtitleUnit.getText().get(1));
        assertEquals("string. Ain't", subtitleUnit.getText().get(2));
        assertEquals("it cool??? :)", subtitleUnit.getText().get(3));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.getNumber());
        assertEquals("00:11:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("00:12:26,600", subtitleUnit.getEndTime().toString());
        assertEquals(5, subtitleUnit.getText().size());
        assertEquals("Hello!!! There", subtitleUnit.getText().get(0));
        assertEquals("is really", subtitleUnit.getText().get(1));
        assertEquals("nothing to see", subtitleUnit.getText().get(2));
        assertEquals("here. Foo Bar.", subtitleUnit.getText().get(3));
        assertEquals("Bye World.", subtitleUnit.getText().get(4));
    }
    
    @Test
    public void testAppendSubtitle() {
        SubtitleService subtitleService = new SubtitleService();
        SubtitleEditor.appendSubtitle(subtitleService, "00:00:24,600", "00:00:26,600", Arrays.asList("Foo"));
        SubtitleEditor.appendSubtitle(subtitleService, "00:11:24,600", "00:12:26,600", Arrays.asList("Bar"));
        
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals(1L, subtitleUnit.getNumber());
        assertEquals("00:00:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("00:00:26,600", subtitleUnit.getEndTime().toString());
        assertEquals(1, subtitleUnit.getText().size());
        assertEquals("Foo", subtitleUnit.getText().get(0));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals(2L, subtitleUnit.getNumber());
        assertEquals("00:11:24,600", subtitleUnit.getStartTime().toString());
        assertEquals("00:12:26,600", subtitleUnit.getEndTime().toString());
        assertEquals(1, subtitleUnit.getText().size());
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
        Assert.assertEquals("Foo1", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo2", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo4", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo5", SubtitleUtils.join(subtitleUnit.getText(), ""));
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
                Collections.singletonList("Foo100"));
        
        assertEquals(5, subtitleService.size());
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals("Foo1", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo2", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo100", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo3", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(5);
        assertEquals("Foo4", SubtitleUtils.join(subtitleUnit.getText(), ""));
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
                    Collections.singletonList("Foo100")));
        
        assertEquals(5, subtitleService.size());
        SubtitleUnit subtitleUnit = subtitleService.get(1);
        assertEquals("Foo1", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo2", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo100", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo3", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(5);
        assertEquals("Foo4", SubtitleUtils.join(subtitleUnit.getText(), ""));
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
        assertEquals("Foo100", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(2);
        assertEquals("Foo1", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(3);
        assertEquals("Foo2", SubtitleUtils.join(subtitleUnit.getText(), ""));
        
        subtitleUnit = subtitleService.get(4);
        assertEquals("Foo3", SubtitleUtils.join(subtitleUnit.getText(), ""));
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
        assertEquals("test", SubtitleUtils.join(s.getText(), ""));
    }
}
