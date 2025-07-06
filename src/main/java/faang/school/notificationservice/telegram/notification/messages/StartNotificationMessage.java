package faang.school.notificationservice.telegram.notification.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StartNotificationMessage {
    SUCCESS_CONNECTED("Chat successfully connected"),
    SUCCESS_ALREADY_CONNECTED("Already connected. Choose another action"),
    FAILED_UNEXPECTED("Failed to connect chat. Try again later"),
    FAILED_NEED_CONNECT("You need connect telegram user name first and set telegram as preferred contact type to use this service");

    private final String message;
}
