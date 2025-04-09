package faang.school.notificationservice.exception;

public class MessageBuilderNotFoundException extends CustomException {

    public MessageBuilderNotFoundException(ExceptionMessage message, String className) {
        super(message, className);
    }
}
