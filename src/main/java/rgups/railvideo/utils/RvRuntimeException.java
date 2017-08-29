package rgups.railvideo.utils;

public class RvRuntimeException extends RuntimeException {

    public RvRuntimeException() {
        super();
    }

    public RvRuntimeException(String message) {
        super(message);
    }

    public RvRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RvRuntimeException(Throwable cause) {
        super(cause);
    }

    protected RvRuntimeException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
