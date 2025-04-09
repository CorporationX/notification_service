package faang.school.notificationservice.service;

public interface MailService {

    void sendEmail(String to, String subject, String text);
}
