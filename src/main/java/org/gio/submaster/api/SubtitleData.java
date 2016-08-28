package org.gio.submaster.api;

import org.gio.submaster.editor.SubtitleEditor;

import java.util.Iterator;
import java.util.TreeSet;

import static org.gio.submaster.api.SubtitleFormatter.longToSrt;

/**
 * This class stores collections of SubtitleUnit objects.
 * 
 * All the methods here perform low-level operations on SubtitleData object.
 * If you need to perform high-level operations for editing the SubtitleData,
 * use {@link SubtitleEditor} instead.
 * 
 *
 */
public class SubtitleData implements Iterable<SubtitleUnit>, Cloneable {
    private final TreeSet<SubtitleUnit> info;
    
    /**
     * Creates a new instance of SubtitleData.
     */
    public SubtitleData() {
        info = new TreeSet<>();
    }
    
    /**
     * Creates a new instance of SubtitleData.
     * This constructor acts as a copy constructor.
     * 
     * @param subtitleData the SubtitleData object
     */
    public SubtitleData(SubtitleData subtitleData) {
        info = new TreeSet<>(subtitleData.info);
    }
    
    /**
     * Adds SubtitleUnit object into SubtitleData object. If SubtitleUnit object already exists, the old
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
     * Gets the number of SubtitleUnit objects stored in SubtitleData object.
     * 
     * @return the number of SubtitleUnit objects stored in SubtitleData object
     */
    public int size() {
        return info.size();
    }
    
    /**
     * Removes the SubtitleUnit object from SubtitleData.
     * 
     * @param subtitleUnit the SubtitleUnit object to be removed from SubtitleData
     */
    public void remove(SubtitleUnit subtitleUnit) {
        info.remove(subtitleUnit);
    }
    
    /**
     * Removes the SubtitleUnit object with subtitle number from SubtitleData.
     * 
     * @param number the subtitle number to be removed from SubtitleData
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
     * Check if the subtitle number is in the SubtitleData object.
     * 
     * @param number the subtitle number
     * @return true if the subtitle number is in the SubtitleData; false otherwise
     */
    public boolean contains(int number) {
        return info.contains(new SubtitleUnit(number, null, null, new String[]{}));
    }
    
    /**
     * Check if the SubtitleUnit is in the SubtitleData object.
     * 
     * @param subtitleUnit the SubtitleUnit object
     * @return true if the subtitle number is in the SubtitleData; false otherwise
     */
    public boolean contains(SubtitleUnit subtitleUnit) {
        return info.contains(subtitleUnit);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        return new SubtitleData(this);
    }
}