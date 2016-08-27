package org.gio.jsrt.exception;

/**
 * An exception while reading an SubtitleUnit file.
 * 
 *
 */
public class SubtitleReaderException extends SubtitleException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause the cause
     */
    public SubtitleReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public SubtitleReaderException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public SubtitleReaderException(Throwable cause) {
        super(cause);
    }
}
