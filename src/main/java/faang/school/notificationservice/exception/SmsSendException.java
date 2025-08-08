package faang.school.notificationservice.exception;

public class SmsSendException extends RuntimeException {

    public SmsSendException(String message) {
        super(message);
    }
}
