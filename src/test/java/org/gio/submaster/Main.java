package org.gio.submaster;

import java.io.File;
import java.text.ParseException;
import java.util.Arrays;

import org.gio.submaster.api.SubtitleUnit;
import org.gio.submaster.api.SubtitleData;
import org.gio.submaster.api.SubtitleReader;
import org.gio.submaster.api.SubtitleFormatter;
import org.gio.submaster.api.SubtitleWriter;
import org.gio.submaster.editor.SubtitleEditor;

public class Main {
    private static void print(SubtitleData info) throws ParseException {
        for (SubtitleUnit s : info) {
            System.out.println("Number: " + s.number);
            System.out.println("Start time: " + s.startTime);
            System.out.println("End time: " + s.endTime);
            System.out.println("Texts:");
            for (String line : s.text) {
                System.out.println("    " + line);
            }
            System.out.println();
        }
    }
    
    private static void testRead() throws ParseException {
        SubtitleData info = SubtitleReader.read(new File("in.srt"));
        print(info);
    }
    
    private static void testWrite() throws ParseException {
        SubtitleData info = new SubtitleData();
        info.add(new SubtitleUnit(1, null, null, "Hello", "World"));
        info.add(new SubtitleUnit(2, null, null, "Bye", "World"));
        
        File f = new File("out1.srt");
        f.deleteOnExit();
        SubtitleWriter.write(f, info);
    }
    
    private static void testEdit() throws ParseException {
        SubtitleData info = SubtitleReader.read(new File("in.srt"));
        SubtitleEditor.updateText(info, 1, 10);
        SubtitleEditor.updateTime(info, 1, SubtitleFormatter.Type.MILLISECOND, 100);
        SubtitleEditor.prependSubtitle(info, "00:00:05,000", "00:00:07,000",
            Arrays.asList("Test"));
        SubtitleEditor.appendSubtitle(info, "00:01:05,000", "00:01:07,000",
            Arrays.asList("Test"));

        print(info);

        // Write it back
        File f = new File("out2.srt");
        f.deleteOnExit();
        SubtitleWriter.write(f, info);
    }
    
    public static void main(String[] args) throws ParseException {
        testRead();
        testWrite();
        testEdit();
    }
}
