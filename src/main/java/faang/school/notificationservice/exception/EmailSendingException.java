package faang.school.notificationservice.exception;

public class EmailSendingException extends EmailNotificationException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
