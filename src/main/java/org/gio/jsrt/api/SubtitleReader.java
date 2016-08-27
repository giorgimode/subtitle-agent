package org.gio.jsrt.api;

import org.gio.jsrt.exception.InvalidSubtitleLineException;
import org.gio.jsrt.exception.SubtitleReaderException;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is responsible for reading an SubtitleUnit file.
 * 
 *
 */
public class SubtitleReader {
    public SubtitleReader() {
    }
    
    /**
     * Reads a srt file and transforms it into SubtitleUnit object.
     * 
     * @param srtFile SubtitleUnit file
     * @return the SubtitleData object
     * @throws InvalidSubtitleLineException thrown when the SubtitleUnit file is invalid
     * @throws SubtitleReaderException thrown while reading SubtitleUnit file
     */
    public static SubtitleData read(File srtFile) throws InvalidSubtitleLineException, SubtitleReaderException {
        if (!srtFile.exists()) {
            throw new SubtitleReaderException(srtFile.getAbsolutePath() + " does not exist");
        }
        if (!srtFile.isFile()) {
            throw new SubtitleReaderException(srtFile.getAbsolutePath() + " is not a regular file");
        }
        
        SubtitleData subtitleData = new SubtitleData();
        try (BufferedReader br = new BufferedReader(new FileReader(srtFile))) {
            while (true) {
                subtitleData.add(parse(br));
            }
        } catch (EOFException e) {
            // Do nothing
        } catch (IOException e) {
            throw new SubtitleReaderException(e);
        }
        
        return subtitleData;
    }
    
    private static SubtitleUnit parse(BufferedReader br) throws IOException, EOFException {
        String nString = br.readLine();
        if (nString == null) {
            throw new EOFException();
        }

        int subtitleNumber = -1;
        try {
            nString = nString.replaceAll("[^\\d]", "");
            subtitleNumber = Integer.parseInt(nString);
        } catch (NumberFormatException e) {
            throw new InvalidSubtitleLineException(
                nString + " has an invalid subtitle number");
        }
        
        String tString = br.readLine();
        if (tString == null) {
            throw new InvalidSubtitleLineException(
                "Start time and end time information is not present");
        }
        String[] times = tString.split(SubtitleTimeFormat.TIME_DELIMITER);
        if (times.length != 2) {
            throw new InvalidSubtitleLineException(
                tString + " needs to be seperated with " + SubtitleTimeFormat.TIME_DELIMITER);
        }
        Date startTime = null;
        try {
            startTime = SubtitleTimeFormat.parse(times[0]);
        } catch (ParseException e) {
            throw new InvalidSubtitleLineException(
                times[0] + " has an invalid time format");
        }
        
        Date endTime = null;
        try {
            endTime = SubtitleTimeFormat.parse(times[1]);
        } catch (ParseException e) {
            throw new InvalidSubtitleLineException(
                times[1] + " has an invalid time format");
        }
        
        List<String> subtitleLines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                break;
            }
            subtitleLines.add(line);
        }
        
        if (subtitleLines.size() == 0) {
            throw new InvalidSubtitleLineException("Missing subtitle text information");
        }
        
        return new SubtitleUnit(subtitleNumber, startTime, endTime, subtitleLines);
    }
}
