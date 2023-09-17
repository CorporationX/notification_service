
package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;
    private final UserServiceClient userServiceClient;
    @Value("${spring.mail.subject}")
    private String subject;

    @Override
    public void sendNotification(Long receiverId, String message) {
        SimpleMailMessage messageEmail = new SimpleMailMessage();
        UserDto receiver = userServiceClient.getUser(receiverId);

        messageEmail.setTo(receiver.getEmail());
        messageEmail.setSubject(subject);
        messageEmail.setText(message);
        mailSender.send(messageEmail);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.EMAIL;
    }
}