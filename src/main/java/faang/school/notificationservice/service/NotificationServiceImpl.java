package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final SmsService smsService;

    @Override
    public void send(UserDto user, String message) {
        if (user.getPreference() == UserDto.PreferredContact.EMAIL) {
            sendEmail(user.getEmail(), message);
        } else if (user.getPreference() == UserDto.PreferredContact.SMS) {
            sendSms(user.getPhone(), message);
        } else {
            log.warn("Unknown contact method for user {}: {}", user.getId(), user.getPreference());
        }
    }

    private void sendEmail(String email, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Like Notification");
            helper.setText(message, false);

            mailSender.send(mimeMessage);
            log.info("Email successfully sent to {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }

    private void sendSms(String phone, String message) {
        if (phone == null || phone.isBlank()) {
            log.error("Error: Phone number is missing");
            return;
        }
        smsService.sendSms(phone, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}