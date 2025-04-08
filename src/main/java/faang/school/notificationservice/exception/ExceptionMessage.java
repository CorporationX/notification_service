package faang.school.notificationservice.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionMessage {
    TELEGRAM_EXCEPTION("Error sending message in telegram"),
    ;

    private final String message;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
