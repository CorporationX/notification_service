package faang.school.notificationservice.service.mail;

public interface EmailService {
    void sendMessage(String to, String subject, String text);
}
