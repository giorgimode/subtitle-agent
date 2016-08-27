package org.gio.jsrt.api;

import org.gio.jsrt.exception.SubtitleWriterException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

/**
 * This class is responsible for writing an SubtitleUnit file.
 * 
 *
 */
public class SubtitleWriter {
    public SubtitleWriter() {
    }
    
    /**
     * Writes a srt file from an SubtitleUnit object.
     * 
     * @param srtFile the srt file
     * @param subtitleData the SubtitleData object
     * @throws SubtitleWriterException thrown while writing an SubtitleUnit file
     */
    public static void write(File srtFile, SubtitleData subtitleData) throws SubtitleWriterException, ParseException {
        try (PrintWriter pw = new PrintWriter(srtFile)) {
            for (SubtitleUnit subtitleUnit : subtitleData) {
                pw.println(subtitleUnit.number);
                pw.println(
                    SubtitleTimeFormat.format(subtitleUnit.startTime) +
                    SubtitleTimeFormat.TIME_DELIMITER +
                    SubtitleTimeFormat.format(subtitleUnit.endTime));
                for (String text : subtitleUnit.text) {
                    pw.println(text);
                }
                // Add an empty line at the end
                pw.println();
            }
        } catch (IOException e) {
            throw new SubtitleWriterException(e);
        }
    }
}
