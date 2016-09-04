package com.giorgimode.subtitle.exception;

/**
 * An exception while writing an SubtitleUnit file.
 * 
 *
 */
public class SubtitleWriterException extends SubtitleException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause the cause
     */
    public SubtitleWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public SubtitleWriterException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public SubtitleWriterException(Throwable cause) {
        super(cause);
    }
}
