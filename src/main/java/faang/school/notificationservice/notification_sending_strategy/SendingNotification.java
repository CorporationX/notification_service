package faang.school.notificationservice.notification_sending_strategy;

public interface SendingNotification {
    void sending(String email, String title, String messageText);
}
