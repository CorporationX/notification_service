package faang.school.notificationservice.telegram;

public interface ActionExecutor {
    NotificationAction getAction(NotificationActionType notificationActionType);
}
