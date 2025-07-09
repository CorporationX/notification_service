package faang.school.notificationservice.telegram.notification.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationBotUpdateConsumerMessage {
    FAILED_ACTION_NOT_SUPPORTED("Action not supported");

    private final String message;
}
