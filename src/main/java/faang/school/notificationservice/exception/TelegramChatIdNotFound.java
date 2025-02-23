package faang.school.notificationservice.exception;

public class TelegramChatIdNotFound extends RuntimeException {

    public TelegramChatIdNotFound(String message) {
        super(message);
    }
}
