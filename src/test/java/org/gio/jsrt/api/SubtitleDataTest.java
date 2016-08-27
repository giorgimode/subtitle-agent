package org.gio.jsrt.api;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import org.gio.jsrt.util.StringUtils;
import org.junit.Test;

public class SubtitleDataTest {

/*   @Test
   public void testAdd() {
       SubtitleData subtitleData = new SubtitleData();
       Date date = new Date();
       subtitleData.add(new SubtitleUnit(2, 0, 0, "Hello", "World"));
       subtitleData.add(new SubtitleUnit(1, 0, 0, "Foo", "Bar"));
       subtitleData.add(new SubtitleUnit(2, 0, 0, "Bye", "World"));
       
       assertEquals(2, subtitleData.size());
       Iterator<SubtitleUnit> iter = subtitleData.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
       assertEquals(SubtitleTimeFormat.format(date), SubtitleTimeFormat.format(subtitleUnit.startTime));
       assertEquals(SubtitleTimeFormat.format(date), SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Foo Bar", StringUtils.join(subtitleUnit.text, " "));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
       assertEquals(SubtitleTimeFormat.format(date), SubtitleTimeFormat.format(subtitleUnit.startTime));
       assertEquals(SubtitleTimeFormat.format(date), SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Bye World", StringUtils.join(subtitleUnit.text, " "));
       
       assertFalse(iter.hasNext());
   }
   
   @Test
   public void testRemove1() {
       SubtitleData subtitleData = new SubtitleData();
       Date d = new Date();
       subtitleData.add(new SubtitleUnit(3, d, d, "Hello", "World"));
       subtitleData.add(new SubtitleUnit(1, d, d, "Foo", "Bar"));
       subtitleData.add(new SubtitleUnit(2, d, d, "Bye", "World"));
       
       subtitleData.remove(3);
       
       assertEquals(2, subtitleData.size());
       Iterator<SubtitleUnit> iter = subtitleData.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
       assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.startTime));
       assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Foo Bar", StringUtils.join(subtitleUnit.text, " "));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
       assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.startTime));
       assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Bye World", StringUtils.join(subtitleUnit.text, " "));
       
       assertFalse(iter.hasNext());
   }
   */
   @Test
   public void testRemove2() throws ParseException {
       SubtitleData subtitleData = new SubtitleData();
       Date d = new Date();
       SubtitleUnit toBeDeleted = new SubtitleUnit(3, 0, 0, "Hello", "World");
       subtitleData.add(toBeDeleted);
       subtitleData.add(new SubtitleUnit(1, 0, 0, "Foo", "Bar"));
       subtitleData.add(new SubtitleUnit(2, 0, 0, "Bye", "World"));
       
       subtitleData.remove(toBeDeleted);
       
       assertEquals(2, subtitleData.size());
       Iterator<SubtitleUnit> iter = subtitleData.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
      // assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.startTime));
    //   assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Foo Bar", StringUtils.join(subtitleUnit.text, " "));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
    //   assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.startTime));
    //   assertEquals(SubtitleTimeFormat.format(d), SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Bye World", StringUtils.join(subtitleUnit.text, " "));
       
       assertFalse(iter.hasNext());
   }

   @Test
   public void testGet() {
       SubtitleData subtitleData = new SubtitleData();
       SubtitleUnit subtitleUnit1 = new SubtitleUnit(1, 0, 0, "Foo", "Bar");
       subtitleData.add(subtitleUnit1);
       SubtitleUnit subtitleUnit2 = new SubtitleUnit(2, 1, 2, "Hello", "World");
       subtitleData.add(subtitleUnit2);
       
       assertEquals(subtitleUnit1, subtitleData.get(1));
       assertEquals(subtitleUnit1, subtitleData.get(subtitleUnit1));
   }
   
   @Test
   public void testContains() {
       SubtitleData subtitleData = new SubtitleData();
       SubtitleUnit subtitleUnit1 = new SubtitleUnit(1, 0, 0, "Foo", "Bar");
       subtitleData.add(subtitleUnit1);
       SubtitleUnit subtitleUnit2 = new SubtitleUnit(2, 0, 0, "Hello", "World");
       subtitleData.add(subtitleUnit2);
       
       assertTrue(subtitleData.contains(1));
       assertTrue(subtitleData.contains(subtitleUnit1));
   }
}
