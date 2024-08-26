package faang.school.notificationservice.exception;

public class CustomTelegramException extends RuntimeException {

    public CustomTelegramException(String message, Throwable cause) {
        super(message, cause);
    }
}