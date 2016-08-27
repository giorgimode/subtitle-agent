package org.gio.jsrt.api;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.gio.jsrt.exception.InvalidSubtitleLineException;
import org.gio.jsrt.exception.SubtitleReaderException;
import org.junit.Test;

/**
 *
 */
public class SubtitleReaderTest {

   @Test
   public void testRead() throws Exception {
       SubtitleData info = SubtitleReader.read(new File("src/test/resources/good1.srt"));
       
       assertEquals(2, info.size());
       Iterator<SubtitleUnit> iter = info.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
       assertEquals("Hello World", subtitleUnit.text.get(0));
       assertEquals("00:00:20,000", SubtitleTimeFormat.format(subtitleUnit.startTime));
       assertEquals("00:00:24,400", SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Bye World", subtitleUnit.text.get(1));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
       assertEquals("00:00:24,600", SubtitleTimeFormat.format(subtitleUnit.startTime));
       assertEquals("00:00:27,800", SubtitleTimeFormat.format(subtitleUnit.endTime));
       assertEquals("Foo Bar", subtitleUnit.text.get(0));
       assertEquals("Bar Foo", subtitleUnit.text.get(1));
   }

    @Test
    public void testReadByTime() throws Exception {
        SubtitleData info = SubtitleReader.read(new File("src/test/resources/good2.srt"));

        assertEquals(464, info.size());
/*
        Iterator<SubtitleUnit> iter = info.iterator();
        SubtitleUnit subtitleUnit = iter.next();
        assertEquals(1, subtitleUnit.number);
        assertEquals("Hello World", subtitleUnit.text.get(0));
        assertEquals("00:00:20,000", SubtitleTimeFormat.format(subtitleUnit.startTime));
        assertEquals("00:00:24,400", SubtitleTimeFormat.format(subtitleUnit.endTime));
        assertEquals("Bye World", subtitleUnit.text.get(1));
*/

    }

    @Test(expected = SubtitleReaderException.class)
   public void testReadFileDoesntExist() {
       SubtitleReader.read(new File("foo.srt"));
   }
   
   @Test(expected = SubtitleReaderException.class)
   public void testReadIsNotAFile() {
       SubtitleReader.read(new File("."));
   }
   
   /*
    * Additional newline after subtitle number 1
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT1() {
       SubtitleReader.read(new File("src/test/resources/bad1.srt"));
   }
   
   /*
    * Missing start time and date time information
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT2() {
       SubtitleReader.read(new File("src/test/resources/bad2.srt"));
   }
   
   /*
    * Invalid start time and date time information
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT3() {
       SubtitleReader.read(new File("src/test/resources/bad3.srt"));
   }
   
   /*
    * Missing subtitle text
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT4() {
       SubtitleReader.read(new File("src/test/resources/bad4.srt"));
   }
}
