package com.giorgimode.subtitle.api;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Iterator;

import com.giorgimode.subtitle.util.SubtitleUtils;
import org.junit.Assert;
import org.junit.Test;

public class SubtitleServiceTest {

   @Test
   public void testAdd() {
       SubtitleService subtitleService = new SubtitleService();
       SRTTime startTime = new SRTTime(0, 0, 15, 120);
       SRTTime startTime2 = new SRTTime(0, 0, 3, 567);
       subtitleService.add(new SubtitleUnit(1, startTime, null, "Foo", "Bar"));
       subtitleService.add(new SubtitleUnit(2, startTime2, null, "Bye", "World"));
       
       assertEquals(2, subtitleService.size());
       Iterator<SubtitleUnit> iter = subtitleService.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.getNumber());
       assertEquals("00:00:03,567", subtitleUnit.getStartTime().toString());
       assertEquals(null, subtitleUnit.getEndTime());
       Assert.assertEquals("Bye World", SubtitleUtils.join(subtitleUnit.getText(), " "));

       // Treeset will order items automatically (by startTime, then by number)
       // Hence elements are reordered when added
       subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.getNumber());
       assertEquals("00:00:15,120", subtitleUnit.getStartTime().toString());
       assertEquals(null, subtitleUnit.getEndTime());
       assertEquals("Foo Bar", SubtitleUtils.join(subtitleUnit.getText(), " "));
       
       assertFalse(iter.hasNext());
   }
   

   @Test
   public void testRemove1() {
       SubtitleService subtitleService = new SubtitleService();
       subtitleService.add(new SubtitleUnit(3, null, null, "Hello", "World"));
       subtitleService.add(new SubtitleUnit(1, null, null, "Foo", "Bar"));
       subtitleService.add(new SubtitleUnit(2, null, null, "Bye", "World"));
       
       subtitleService.remove(3);
       
       assertEquals(2, subtitleService.size());
       Iterator<SubtitleUnit> iter = subtitleService.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.getNumber());
       assertEquals(null, subtitleUnit.getStartTime());
       assertEquals(null, subtitleUnit.getEndTime());
       assertEquals("Foo Bar", SubtitleUtils.join(subtitleUnit.getText(), " "));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.getNumber());
       assertEquals(null, subtitleUnit.getStartTime());
       assertEquals(null, subtitleUnit.getEndTime());
       assertEquals("Bye World", SubtitleUtils.join(subtitleUnit.getText(), " "));
       
       assertFalse(iter.hasNext());
   }


   @Test
   public void testRemove2() throws ParseException {
       SRTTime startTime = new SRTTime(0, 0, 1, 0);
       SRTTime endTime = new SRTTime(0, 0, 3, 5);
       SRTTime startTime2 = new SRTTime(0, 0, 0, 0);
       SRTTime endTime2 = new SRTTime(0, 0, 0, 5);

       SubtitleService subtitleService = new SubtitleService();
       SubtitleUnit toBeDeleted = new SubtitleUnit(3, startTime, endTime, "Hello", "World");
       subtitleService.add(toBeDeleted);
       subtitleService.add(new SubtitleUnit(1, null, null, "Foo", "Bar"));


       subtitleService.add(new SubtitleUnit(2, startTime2, endTime2, "Bye", "World"));
       
       subtitleService.remove(toBeDeleted);
       
       assertEquals(2, subtitleService.size());
       Iterator<SubtitleUnit> iter = subtitleService.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.getNumber());
       assertEquals("Foo Bar", SubtitleUtils.join(subtitleUnit.getText(), " "));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.getNumber());
       assertEquals("00:00:00,000", subtitleUnit.getStartTime().toString());
       assertEquals("00:00:00,005", subtitleUnit.getEndTime().toString());
       assertEquals("Bye World", SubtitleUtils.join(subtitleUnit.getText(), " "));
       
       assertFalse(iter.hasNext());
   }

   @Test
   public void testGet() {
       SubtitleService subtitleService = new SubtitleService();
       SubtitleUnit subtitleUnit1 = new SubtitleUnit(1, null, null, "Foo", "Bar");
       subtitleService.add(subtitleUnit1);
       SubtitleUnit subtitleUnit2 = new SubtitleUnit(2, null, null, "Hello", "World");
       subtitleService.add(subtitleUnit2);
       
       assertEquals(subtitleUnit1, subtitleService.get(1));
       assertEquals(subtitleUnit1, subtitleService.get(subtitleUnit1));
   }
   
   @Test
   public void testContains() {
       SubtitleService subtitleService = new SubtitleService();
       SubtitleUnit subtitleUnit1 = new SubtitleUnit(1, null, null, "Foo", "Bar");
       subtitleService.add(subtitleUnit1);
       SubtitleUnit subtitleUnit2 = new SubtitleUnit(2, null, null, "Hello", "World");
       subtitleService.add(subtitleUnit2);
       
       assertTrue(subtitleService.contains(1));
       assertTrue(subtitleService.contains(subtitleUnit1));
   }
}
