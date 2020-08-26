package net.anotheria.anoplass.access.aop.exception;

import net.anotheria.anoplass.api.APIException;

/**
 * This exception is supposed to be thrown when operation can't be performed due to some reason.
 */
public class OperationDeniedException extends APIException {

    /**
     * Default Constructor.
     *
     */
    public OperationDeniedException() {
    }

    /**
     * Constructor.
     *
     * @param message fail message
     */
    public OperationDeniedException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message fail message
     * @param cause fail cause
     */
    public OperationDeniedException(String message, Exception cause) {
        super(message, cause);
    }
}
