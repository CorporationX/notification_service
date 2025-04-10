package faang.school.notificationservice.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionMessage {
    TELEGRAM_EXCEPTION("Error sending message in telegram"),
    PREFERENCE_NOT_FOUND("User with id %d doesn't have a preferred method of communication"),
    EVENT_READ_EXCEPTION("Error reading event from message"),
    MESSAGE_BUILDER_NOT_FOUND("MessageBuilder for %s class not found"),
    ;

    private final String message;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
