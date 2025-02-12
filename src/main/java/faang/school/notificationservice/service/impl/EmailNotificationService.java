package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final EmailService emailService;

    @Override
    public void send(UserDto user, String message) {
        emailService.sendSimpleMessage(user.getEmail(), "", message);
        log.info("Email notification was sent to user {}", user);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }
}
