package faang.school.notificationservice.exception.messaging;

public class MessageBuilderNotFoundException extends RuntimeException {

    public MessageBuilderNotFoundException(String messageType) {
        super(String.format("No MessageBuilder found for class %s", messageType));;
    }
}
