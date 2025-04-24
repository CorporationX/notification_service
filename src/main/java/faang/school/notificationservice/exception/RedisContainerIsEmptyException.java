package faang.school.notificationservice.exception;

public class RedisContainerIsEmptyException extends RuntimeException {
    public RedisContainerIsEmptyException(String message) {
        super(message);
    }
}
