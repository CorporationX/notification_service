package faang.school.notificationservice.service.telegram;

import lombok.Getter;

@Getter
public enum TelegramLabel {
    REGISTRATION_SUCCESS("registration.success"),
    REGISTRATION_ERROR("registration.error"),
    CANCEL_REGISTRATION_SUCCESS("cancel.registration.success"),
    CANCEL_REGISTRATION_ERROR("cancel.registration.error"),
    REGISTER_USER_IN_APP("register.hello"),
    REGISTER_PHONE("register.phone"),
    COMMAND_LIST("command.list");

    private final String label;

    TelegramLabel(String label) {
        this.label = label;
    }
}
