package com.giorgimode.subtitle.api;

import com.giorgimode.subtitle.exception.SubtitleWriterException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

/**
 * This class is responsible for writing an SubtitleUnit file.
 */
public final class SubtitleWriter {
    private SubtitleWriter() {
    }

    /**
     * Writes a srt file from an SubtitleUnit object.
     *
     * @param srtFile         the srt file
     * @param subtitleService the SubtitleService object
     * @throws SubtitleWriterException thrown while writing an SubtitleUnit file
     */
    public static void write(File srtFile, SubtitleService subtitleService) throws SubtitleWriterException, ParseException {
        try (PrintWriter pw = new PrintWriter(srtFile)) {
            for (SubtitleUnit subtitleUnit : subtitleService) {
                pw.println(subtitleUnit.getNumber());
                pw.println(
                        subtitleUnit.getStartTime().toString()
                        + SubtitleFormatter.TIME_DELIMITER
                        + subtitleUnit.getEndTime().toString());
                for (String text : subtitleUnit.getText()) {
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
