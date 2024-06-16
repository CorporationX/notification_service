package faang.school.notificationservice.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ListenerExceptionMessage {
    MESSAGE_BUILDER_NOT_FOUND("There is no message builder for received event type"),
    NO_SUCH_USER("User with received id was not found");

    private final String message;
}
