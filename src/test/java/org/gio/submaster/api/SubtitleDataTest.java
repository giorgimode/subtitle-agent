package org.gio.submaster.api;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Iterator;

import org.gio.submaster.util.StringUtils;
import org.junit.Test;

public class SubtitleDataTest {

   @Test
   public void testAdd() {
       SubtitleData subtitleData = new SubtitleData();
       SRTTime startTime = new SRTTime(0, 0, 15, 120);
       SRTTime startTime2 = new SRTTime(0, 0, 3, 567);
       subtitleData.add(new SubtitleUnit(1, startTime, null, "Foo", "Bar"));
       subtitleData.add(new SubtitleUnit(2, startTime2, null, "Bye", "World"));
       
       assertEquals(2, subtitleData.size());
       Iterator<SubtitleUnit> iter = subtitleData.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
       assertEquals("00:00:03,567", subtitleUnit.startTime.toString());
       assertEquals(null, subtitleUnit.endTime);
       assertEquals("Bye World", StringUtils.join(subtitleUnit.text, " "));

       // Treeset will order items automatically (by startTime, then by number)
       // Hence elements are reordered when added
       subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
       assertEquals("00:00:15,120", subtitleUnit.startTime.toString());
       assertEquals(null, subtitleUnit.endTime);
       assertEquals("Foo Bar", StringUtils.join(subtitleUnit.text, " "));
       
       assertFalse(iter.hasNext());
   }
   

   @Test
   public void testRemove1() {
       SubtitleData subtitleData = new SubtitleData();
       subtitleData.add(new SubtitleUnit(3, null, null, "Hello", "World"));
       subtitleData.add(new SubtitleUnit(1, null, null, "Foo", "Bar"));
       subtitleData.add(new SubtitleUnit(2, null, null, "Bye", "World"));
       
       subtitleData.remove(3);
       
       assertEquals(2, subtitleData.size());
       Iterator<SubtitleUnit> iter = subtitleData.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
       assertEquals(null, subtitleUnit.startTime);
       assertEquals(null, subtitleUnit.endTime);
       assertEquals("Foo Bar", StringUtils.join(subtitleUnit.text, " "));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
       assertEquals(null, subtitleUnit.startTime);
       assertEquals(null, subtitleUnit.endTime);
       assertEquals("Bye World", StringUtils.join(subtitleUnit.text, " "));
       
       assertFalse(iter.hasNext());
   }


   @Test
   public void testRemove2() throws ParseException {
       SRTTime startTime = new SRTTime(0, 0, 1, 0);
       SRTTime endTime = new SRTTime(0, 0, 3, 5);
       SRTTime startTime2 = new SRTTime(0, 0, 0, 0);
       SRTTime endTime2 = new SRTTime(0, 0, 0, 5);

       SubtitleData subtitleData = new SubtitleData();
       SubtitleUnit toBeDeleted = new SubtitleUnit(3, startTime, endTime, "Hello", "World");
       subtitleData.add(toBeDeleted);
       subtitleData.add(new SubtitleUnit(1, null, null, "Foo", "Bar"));


       subtitleData.add(new SubtitleUnit(2, startTime2, endTime2, "Bye", "World"));
       
       subtitleData.remove(toBeDeleted);
       
       assertEquals(2, subtitleData.size());
       Iterator<SubtitleUnit> iter = subtitleData.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
       assertEquals("Foo Bar", StringUtils.join(subtitleUnit.text, " "));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
       assertEquals("00:00:00,000", subtitleUnit.startTime.toString());
       assertEquals("00:00:00,005", subtitleUnit.endTime.toString());
       assertEquals("Bye World", StringUtils.join(subtitleUnit.text, " "));
       
       assertFalse(iter.hasNext());
   }

   @Test
   public void testGet() {
       SubtitleData subtitleData = new SubtitleData();
       SubtitleUnit subtitleUnit1 = new SubtitleUnit(1, null, null, "Foo", "Bar");
       subtitleData.add(subtitleUnit1);
       SubtitleUnit subtitleUnit2 = new SubtitleUnit(2, null, null, "Hello", "World");
       subtitleData.add(subtitleUnit2);
       
       assertEquals(subtitleUnit1, subtitleData.get(1));
       assertEquals(subtitleUnit1, subtitleData.get(subtitleUnit1));
   }
   
   @Test
   public void testContains() {
       SubtitleData subtitleData = new SubtitleData();
       SubtitleUnit subtitleUnit1 = new SubtitleUnit(1, null, null, "Foo", "Bar");
       subtitleData.add(subtitleUnit1);
       SubtitleUnit subtitleUnit2 = new SubtitleUnit(2, null, null, "Hello", "World");
       subtitleData.add(subtitleUnit2);
       
       assertTrue(subtitleData.contains(1));
       assertTrue(subtitleData.contains(subtitleUnit1));
   }
}
