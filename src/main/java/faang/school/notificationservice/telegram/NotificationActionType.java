package faang.school.notificationservice.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationActionType {
    START("/start"),
    HELP("/help");

    private final String action;

    public static NotificationActionType fromString(String action) {
        for (NotificationActionType actionType : NotificationActionType.values()) {
            if (actionType.getAction().equalsIgnoreCase(action)) {
                return actionType;
            }
        }

        throw new IllegalArgumentException("No contact action with name " + action + " found");
    }
}
