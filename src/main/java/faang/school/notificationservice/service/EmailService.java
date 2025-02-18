package faang.school.notificationservice.service;

public interface EmailService {
    void sendSimpleMessage(
            String to,
            String subject,
            String text);
}
