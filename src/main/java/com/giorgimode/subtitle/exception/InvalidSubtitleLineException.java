package com.giorgimode.subtitle.exception;

/**
 * An exception for an invalid SubtitleUnit format.
 * 
 *
 */
public class InvalidSubtitleLineException extends SubtitleException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause the cause
     */
    public InvalidSubtitleLineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public InvalidSubtitleLineException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public InvalidSubtitleLineException(Throwable cause) {
        super(cause);
    }
}
