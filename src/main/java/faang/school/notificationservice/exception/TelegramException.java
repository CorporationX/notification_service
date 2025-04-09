package faang.school.notificationservice.exception;

public class TelegramException extends CustomException {

    public TelegramException(ExceptionMessage message, Throwable cause) {
        super(message, cause);
    }
}
