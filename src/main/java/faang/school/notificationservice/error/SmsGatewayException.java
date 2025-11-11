package faang.school.notificationservice.error;

public class SmsGatewayException extends RuntimeException {
    public SmsGatewayException(String message) { super(message); }
    public SmsGatewayException(String message, Throwable cause) { super(message, cause); }
}