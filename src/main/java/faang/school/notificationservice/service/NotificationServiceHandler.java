package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationServiceHandler {
    private final Map<UserDto.PreferredContact, NotificationService> notificationServices;
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceHandler.class);

    public NotificationServiceHandler(Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    public void sendNotification(UserDto user, String message) {
        NotificationService notificationService = notificationServices.get(user.getPreference());
        if (notificationService == null) {
            log.warn("There is no NotificationService for preference:  {}", user.getPreference());
            return;
        }

        try {
            log.info("Sending a notification to the user {} via {}", user.getUsername(), user.getPreference());
            notificationService.send(user, message);
        } catch (Exception e) {
            log.error("Error sending notification", e);
        }
    }
}