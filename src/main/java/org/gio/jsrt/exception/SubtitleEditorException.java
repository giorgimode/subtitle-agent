package org.gio.jsrt.exception;

import org.gio.jsrt.exception.SubtitleException;

/**
 *
 */
public class SubtitleEditorException extends SubtitleException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause the cause
     */
    public SubtitleEditorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     */
    public SubtitleEditorException(String message) {
        super(message);
    }

    /**
     * @param cause the cause
     */
    public SubtitleEditorException(Throwable cause) {
        super(cause);
    }
}
