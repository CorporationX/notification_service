package faang.school.notificationservice.exception;

public class MessageBuilderNotFoundException extends RuntimeException {
    public MessageBuilderNotFoundException(String eventType) {
        super("Message builder not found for event type: " + eventType);
    }
}
