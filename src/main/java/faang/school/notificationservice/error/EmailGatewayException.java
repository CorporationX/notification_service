package faang.school.notificationservice.error;

public class EmailGatewayException extends RuntimeException {
    public EmailGatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}