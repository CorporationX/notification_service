package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MailService;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailNotificationService implements NotificationService {

    private final MailService emailService;

    private final static String EMAIL_SUBJECT = "Griffon team - sending mail";

    @Override
    public void send(UserDto user, String message) {
        if (user.getPreference() != UserDto.PreferredContact.EMAIL) {
            log.debug("User {} prefers {}, email not sent", user.getId(), user.getPreference());
            return;
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Empty email for user {}: {}", user.getId(), user.getEmail());
            return;
        }
        emailService.sendEmail(user.getEmail(), EMAIL_SUBJECT, message);
        log.info("Email notification was sent to user {}", user.getId());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
