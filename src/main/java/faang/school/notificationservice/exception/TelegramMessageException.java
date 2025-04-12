package faang.school.notificationservice.exception;

import lombok.Getter;

@Getter
public class TelegramMessageException extends RuntimeException {

    private final Long chatId;

    public TelegramMessageException(String message, Long chatId, Throwable cause) {
        super(message, cause);
        this.chatId = chatId;
    }
}
