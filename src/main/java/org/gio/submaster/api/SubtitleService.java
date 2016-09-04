package org.gio.submaster.api;

import org.gio.submaster.editor.SubtitleEditor;

import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;

import static org.gio.submaster.api.SubtitleFormatter.longToSrt;
import static org.gio.submaster.util.StringUtils.convertSubtitleUnit;

/**
 * This class stores collections of SubtitleUnit objects.
 * 
 * All the methods here perform low-level operations on SubtitleService object.
 * If you need to perform high-level operations for editing the SubtitleService,
 * use {@link SubtitleEditor} instead.
 * 
 *
 */
public class SubtitleService implements Iterable<SubtitleUnit>, Cloneable {
    private final TreeSet<SubtitleUnit> info;
    
    /**
     * Creates a new instance of SubtitleService.
     */
    public SubtitleService() {
        info = new TreeSet<>();
    }

    public SubtitleService(File subtitleFile) {
        info = new TreeSet<>();
        SubtitleReader.read(this, subtitleFile);
    }
    
    /**
     * Creates a new instance of SubtitleService.
     * This constructor acts as a copy constructor.
     * 
     * @param subtitleService the SubtitleService object
     */
    public SubtitleService(SubtitleService subtitleService) {
        info = new TreeSet<>(subtitleService.info);
    }

    public void addSubtitleFile(File subtitleFile){
            SubtitleReader.read(this, subtitleFile);
    }

    /**
     * Adds SubtitleUnit object into SubtitleService object. If SubtitleUnit object already exists, the old
     * SubtitleUnit object will be replaced with the new SubtitleUnit object.
     * 
     * @param subtitleUnit the SubtitleUnit object to be added
     */
    public void add(SubtitleUnit subtitleUnit) {
        remove(subtitleUnit);
        info.add(subtitleUnit);
    }
    
    /**
     * {@inheritDoc}
     */
    public Iterator<SubtitleUnit> iterator() {
        return info.iterator();
    }
    
    /**
     * Gets the number of SubtitleUnit objects stored in SubtitleService object.
     * 
     * @return the number of SubtitleUnit objects stored in SubtitleService object
     */
    public int size() {
        return info.size();
    }
    
    /**
     * Removes the SubtitleUnit object from SubtitleService.
     * 
     * @param subtitleUnit the SubtitleUnit object to be removed from SubtitleService
     */
    public void remove(SubtitleUnit subtitleUnit) {
        info.remove(subtitleUnit);
    }
    
    /**
     * Removes the SubtitleUnit object with subtitle number from SubtitleService.
     * 
     * @param number the subtitle number to be removed from SubtitleService
     */
    public void remove(int number) {
        info.remove(get(number));
        
    }
    
    /**
     * Gets the SubtitleUnit object from a given number.
     * 
     * @param number the subtitle number
     * @return the SubtitleUnit object
     */
    public SubtitleUnit get(int number) {
        // Create a dummy SubtitleUnit object since the comparison is by number or by startTime.
        return info.floor(new SubtitleUnit(number, null, null, new String[]{}));
    }

    /**
     * Gets the SubtitleUnit object from a given number.
     *
     * @param startTime the subtitle start time
     * @return the SubtitleUnit object
     */
    public SubtitleUnit get(SRTTime startTime) {
        // Create a dummy SubtitleUnit object since the comparison is by number only.
        return info.floor(new SubtitleUnit(0, startTime, null, new String[]{}));
    }

    public SubtitleUnit get(long timestamp) {
        SRTTime srtTime = longToSrt(timestamp);
        // Create a dummy SubtitleUnit object since the comparison is by number only.
        return info.floor(new SubtitleUnit(0, srtTime, null, new String[]{}));
    }

    public String[][] getCurrentWords(long timestamp) {
        SubtitleUnit subtitleUnit = get(timestamp);
        return convertSubtitleUnit(subtitleUnit);
    }

    /**
     * Gets the SubtitleUnit object.
     * 
     * @param subtitleUnit the SubtitleUnit object
     * @return the SubtitleUnit object
     */
    public SubtitleUnit get(SubtitleUnit subtitleUnit) {
        return info.floor(subtitleUnit);
    }
    
    /**
     * Check if the subtitle number is in the SubtitleService object.
     * 
     * @param number the subtitle number
     * @return true if the subtitle number is in the SubtitleService; false otherwise
     */
    public boolean contains(int number) {
        return info.contains(new SubtitleUnit(number, null, null, new String[]{}));
    }
    
    /**
     * Check if the SubtitleUnit is in the SubtitleService object.
     * 
     * @param subtitleUnit the SubtitleUnit object
     * @return true if the subtitle number is in the SubtitleService; false otherwise
     */
    public boolean contains(SubtitleUnit subtitleUnit) {
        return info.contains(subtitleUnit);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        return new SubtitleService(this);
    }
}