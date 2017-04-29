package com.giorgimode.subtitle.api;

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
        SubtitleService inInfo = new SubtitleService();
        inInfo.add(new SubtitleUnit(2, SubtitleFormatter.stringToSrt("00:00:24,600"),
                SubtitleFormatter.stringToSrt("00:00:27,800"), "Foo Bar", "Bar Foo"));
        inInfo.add(new SubtitleUnit(1, SubtitleFormatter.stringToSrt("00:00:20,000"),
            SubtitleFormatter.stringToSrt("00:00:24,400"), "Hello World", "Bye World"));
            
        File srtFile = new File("src/test/resources/test1.srt");
        SubtitleWriter.write(srtFile, inInfo);
        
        SubtitleService outInfo = new SubtitleService(srtFile);
        assertEquals(inInfo.size(), outInfo.size());
        Iterator<SubtitleUnit> inIter = inInfo.iterator();
        Iterator<SubtitleUnit> outIter = outInfo.iterator();
        
        SubtitleUnit inSubtitleUnit = inIter.next();
        SubtitleUnit outSubtitleUnit = outIter.next();
        assertEquals(inSubtitleUnit.getNumber(), outSubtitleUnit.getNumber());
        assertEquals(inSubtitleUnit.getStartTime().toString(), outSubtitleUnit.getStartTime().toString());
        assertEquals(inSubtitleUnit.getEndTime().toString(), outSubtitleUnit.getEndTime().toString());
        assertEquals(inSubtitleUnit.getText().get(0), outSubtitleUnit.getText().get(0));
        assertEquals(inSubtitleUnit.getText().get(1), outSubtitleUnit.getText().get(1));
        
        inSubtitleUnit = inIter.next();
        outSubtitleUnit = outIter.next();
        assertEquals(inSubtitleUnit.getNumber(), outSubtitleUnit.getNumber());
        assertEquals(inSubtitleUnit.getStartTime().toString(), outSubtitleUnit.getStartTime().toString());
        assertEquals(inSubtitleUnit.getEndTime().toString(), outSubtitleUnit.getEndTime().toString());
        assertEquals(inSubtitleUnit.getText().get(0), outSubtitleUnit.getText().get(0));
        assertEquals(inSubtitleUnit.getText().get(1), outSubtitleUnit.getText().get(1));
    }
}
