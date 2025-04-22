package faang.school.notificationservice.exception;

public class BuildMessageFailedException extends RuntimeException {
    public BuildMessageFailedException(String message) {
        super(message);
    }

    public BuildMessageFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
