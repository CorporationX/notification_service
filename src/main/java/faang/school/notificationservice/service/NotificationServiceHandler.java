package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceHandler {
    private final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    public void sendNotification(UserDto user, String message) {
        NotificationService notificationService = notificationServices.get(user.getPreference());
        if (notificationService == null) {
            log.warn("No NotificationService found for: {}", user.getPreference());
            return;
        }

        try {
            log.info("Sending notification to {} via {}", user.getUsername(), user.getPreference());
            notificationService.send(user, message);
        } catch (Exception e) {
            log.error("Error sending notification", e);
        }
    }
}