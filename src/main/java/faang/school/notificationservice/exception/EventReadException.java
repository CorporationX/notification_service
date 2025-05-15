package faang.school.notificationservice.exception;

public class EventReadException extends CustomException {

    public EventReadException(ExceptionMessage exceptionMessage, Object... args) {
        super(exceptionMessage, args);
    }

    public EventReadException(ExceptionMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
