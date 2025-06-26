package faang.school.notificationservice.exception;

public class MessageBuilderNotFoundException extends RuntimeException {
    public MessageBuilderNotFoundException(String eventType) {
        super(String.format("Message builder not found for event type: %s", eventType));
    }
}
