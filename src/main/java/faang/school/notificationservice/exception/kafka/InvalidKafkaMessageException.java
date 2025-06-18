package faang.school.notificationservice.exception.kafka;

public class InvalidKafkaMessageException extends RuntimeException {
    public InvalidKafkaMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
