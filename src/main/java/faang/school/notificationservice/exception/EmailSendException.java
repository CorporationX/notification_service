package faang.school.notificationservice.exception;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String email, Throwable cause) {
        super("Failed to send email to " + email, cause);
    }
}
