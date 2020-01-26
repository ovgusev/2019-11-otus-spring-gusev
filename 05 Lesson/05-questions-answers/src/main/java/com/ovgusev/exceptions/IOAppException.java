package com.ovgusev.exceptions;

public class IOAppException extends AppException {
    public IOAppException() {
    }

    public IOAppException(String message) {
        super(message);
    }

    public IOAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public IOAppException(Throwable cause) {
        super(cause);
    }

    public IOAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
