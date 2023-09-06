package faang.school.notificationservice.exception;

public class CustomTelegramApiException extends RuntimeException {
    public CustomTelegramApiException(String message) {
        super(message);
    }
}
