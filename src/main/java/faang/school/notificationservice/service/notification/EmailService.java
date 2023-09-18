
package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final UserServiceClient userServiceClient;
    @Value("${spring.mail.subject}")
    private String subject;

    @Override
    public void sendNotification(Long receiverId, String message) {
        UserDto receiver = userServiceClient.getUser(receiverId);

        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        mailMessage.setTo(receiver.getEmail());
        mailMessage.setText(message);
        mailMessage.setSubject(subject);

        javaMailSender.send(mailMessage);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.EMAIL;
    }
}