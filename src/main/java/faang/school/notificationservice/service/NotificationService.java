package faang.school.notificationservice.service;

public interface NotificationService {
    void sendMail(String recipient, String subject, String text);
}
