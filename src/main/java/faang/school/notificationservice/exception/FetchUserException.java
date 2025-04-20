package faang.school.notificationservice.exception;

public class FetchUserException extends RuntimeException {
    public FetchUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
