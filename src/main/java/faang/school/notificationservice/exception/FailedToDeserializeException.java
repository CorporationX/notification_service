package faang.school.notificationservice.exception;

public class FailedToDeserializeException extends RuntimeException {
    public FailedToDeserializeException(String value) {
        super("Failed to deserialize value: " + value);
    }
}
