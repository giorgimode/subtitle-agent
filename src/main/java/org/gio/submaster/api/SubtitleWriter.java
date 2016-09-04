package org.gio.submaster.api;

import org.gio.submaster.exception.SubtitleWriterException;

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
     * @param subtitleService the SubtitleService object
     * @throws SubtitleWriterException thrown while writing an SubtitleUnit file
     */
    public static void write(File srtFile, SubtitleService subtitleService) throws SubtitleWriterException, ParseException {
        try (PrintWriter pw = new PrintWriter(srtFile)) {
            for (SubtitleUnit subtitleUnit : subtitleService) {
                pw.println(subtitleUnit.number);
                pw.println(
                    subtitleUnit.startTime.toString() +
                    SubtitleFormatter.TIME_DELIMITER +
                    subtitleUnit.endTime.toString());
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
