package org.gio.submaster.api;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.Test;


public class SubtitleWriterTest {
    @AfterClass
    public static void cleanUp() {
        new File("src/test/resources/test1.srt").delete();
    }
    
    @Test
    public void testWrite() throws Exception {
        SubtitleData inInfo = new SubtitleData();
        inInfo.add(new SubtitleUnit(2, SubtitleTimeFormat.parse("00:00:24,600"),
                SubtitleTimeFormat.parse("00:00:27,800"), "Foo Bar", "Bar Foo"));
        inInfo.add(new SubtitleUnit(1, SubtitleTimeFormat.parse("00:00:20,000"),
            SubtitleTimeFormat.parse("00:00:24,400"), "Hello World", "Bye World"));
            
        File srtFile = new File("src/test/resources/test1.srt");
        SubtitleWriter.write(srtFile, inInfo);
        
        SubtitleData outInfo = SubtitleReader.read(srtFile);
        assertEquals(inInfo.size(), outInfo.size());
        Iterator<SubtitleUnit> inIter = inInfo.iterator();
        Iterator<SubtitleUnit> outIter = outInfo.iterator();
        
        SubtitleUnit inSubtitleUnit = inIter.next();
        SubtitleUnit outSubtitleUnit = outIter.next();
        assertEquals(inSubtitleUnit.number, outSubtitleUnit.number);
        assertEquals(inSubtitleUnit.startTime.toString(), outSubtitleUnit.startTime.toString());
        assertEquals(inSubtitleUnit.endTime.toString(), outSubtitleUnit.endTime.toString());
        assertEquals(inSubtitleUnit.text.get(0), outSubtitleUnit.text.get(0));
        assertEquals(inSubtitleUnit.text.get(1), outSubtitleUnit.text.get(1));
        
        inSubtitleUnit = inIter.next();
        outSubtitleUnit = outIter.next();
        assertEquals(inSubtitleUnit.number, outSubtitleUnit.number);
        assertEquals(inSubtitleUnit.startTime.toString(), outSubtitleUnit.startTime.toString());
        assertEquals(inSubtitleUnit.endTime.toString(), outSubtitleUnit.endTime.toString());
        assertEquals(inSubtitleUnit.text.get(0), outSubtitleUnit.text.get(0));
        assertEquals(inSubtitleUnit.text.get(1), outSubtitleUnit.text.get(1));
    }
}
