package org.gio.submaster.exception;

/**
 * Any exceptions related to SubtitleUnit.
 * 
 *
 */
public class SubtitleException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause the cause
     */
    public SubtitleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public SubtitleException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public SubtitleException(Throwable cause) {
        super(cause);
    }
}
