package com.giorgimode.subtitle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import com.giorgimode.subtitle.exception.InvalidSubtitleLineException;
import com.giorgimode.subtitle.exception.SubtitleReaderException;
import org.junit.Test;

/**
 *
 */
public class SubtitleReaderTest {

   @Test
   public void testRead() throws Exception {
       SubtitleService subtitleService = new SubtitleService(new File("src/test/resources/good1.srt"));
       
       assertEquals(2, subtitleService.size());
       Iterator<SubtitleUnit> iter = subtitleService.iterator();
       SubtitleUnit subtitleUnit = iter.next();
       assertEquals(1, subtitleUnit.number);
       assertEquals("Hello World", subtitleUnit.text.get(0));
       assertEquals("00:00:20,000", subtitleUnit.startTime.toString());
       assertEquals("00:00:24,400", subtitleUnit.endTime.toString());
       assertEquals("Bye World", subtitleUnit.text.get(1));
       
       subtitleUnit = iter.next();
       assertEquals(2, subtitleUnit.number);
       assertEquals("00:00:24,600", subtitleUnit.startTime.toString());
       assertEquals("00:00:27,800", subtitleUnit.endTime.toString());
       assertEquals("Foo Bar", subtitleUnit.text.get(0));
       assertEquals("Bar Foo", subtitleUnit.text.get(1));
   }

    @Test
    public void testReadByTime() throws Exception {
        SubtitleService info = new SubtitleService(new File("src/test/resources/good2.srt"));

        assertEquals(772, info.size());

        Iterator<SubtitleUnit> iter = info.iterator();
        SubtitleUnit subtitleUnit = iter.next();
        assertEquals(1, subtitleUnit.number);
        assertEquals("Freeman: Inside your head", subtitleUnit.text.get(0));
        assertEquals("is an unexplored world.", subtitleUnit.text.get(1));
        assertEquals("00:00:02,094", subtitleUnit.startTime.toString());
        assertEquals("00:00:06,864", subtitleUnit.endTime.toString());

        // long value must not be mixed with integer value
        subtitleUnit = info.get(446336L);
        assertEquals(121, subtitleUnit.number);
        assertEquals("So when you look down, you don't", subtitleUnit.text.get(0));
        assertEquals("see your own body anymore.", subtitleUnit.text.get(1));
        assertEquals("00:07:25,960", subtitleUnit.startTime.toString());
        assertEquals("00:07:28,761", subtitleUnit.endTime.toString());

        // long value must not be mixed with integer value
        SRTTime srtTime = new SRTTime(0, 20, 43, 900);
        subtitleUnit = info.get(srtTime);
        assertEquals(384, subtitleUnit.number);
        assertEquals("Like, I could see either", subtitleUnit.text.get(0));
        assertEquals("yours as background story,", subtitleUnit.text.get(1));
        assertEquals("00:20:42,855", subtitleUnit.startTime.toString());
        assertEquals("00:20:45,556", subtitleUnit.endTime.toString());

    }

    @Test(expected = SubtitleReaderException.class)
   public void testReadFileDoesntExist() {
        new SubtitleService(new File("foo.srt"));

   }
   
   @Test(expected = SubtitleReaderException.class)
   public void testReadIsNotAFile() {
       new SubtitleService(new File("."));
   }
   
   /*
    * Additional newline after subtitle number 1
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT1() {
       new SubtitleService(new File("src/test/resources/bad1.srt"));

   }
   
   /*
    * Missing start time and date time information
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT2() {
       new SubtitleService(new File("src/test/resources/bad2.srt"));

   }
   
   /*
    * Invalid start time and date time information
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT3() {
       new SubtitleService(new File("src/test/resources/bad3.srt"));

   }
   
   /*
    * Missing subtitle text
    */
   @Test(expected = InvalidSubtitleLineException.class)
   public void testReadInvalidSRT4() {
       new SubtitleService(new File("src/test/resources/bad4.srt"));

   }
}
