package faang.school.notificationservice.service;

import java.io.InputStream;

public interface EmailService {
    void sendSimpleMessage(
            String to,
            String subject,
            String text);
    void sendMessageWithAttachment (
            String to,
            String subject,
            String text,
            String pathToAttachment);

    void sendMessageWithInputStreamAttachment(
            String to,
            String subject,
            String text,
            String attachmentName,
            InputStream attachmentStream);
}
