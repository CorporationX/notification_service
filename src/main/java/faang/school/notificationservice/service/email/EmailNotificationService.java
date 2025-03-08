package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserProfileDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender mailSender;

    @Override
    public void send(UserProfileDto user, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("New Notification");
        email.setText(message);
        mailSender.send(email);
    }

    @Override
    public UserProfileDto.PreferredContact getPreferredContact() {
        return UserProfileDto.PreferredContact.EMAIL;
    }

    @Override
    public boolean supports(UserProfileDto.PreferredContact preferredContact) {
        return getPreferredContact() == preferredContact;
    }
}