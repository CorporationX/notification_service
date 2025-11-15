package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NoPreferredContactException;
import faang.school.notificationservice.exception.NotificationServiceNotFoundException;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationDispatcher {

    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    public void dispatch(long userId, String message) {
        try {
            UserDto user = fetchUser(userId);
            validateUserPreference(user);

            NotificationService service = selectNotificationService(user.getPreference());
            sendNotification(user, message, service);

        } catch (Exception e) {
            log.error("Failed to dispatch notification to user {}", userId, e);
            throw e;
        }
    }

    private UserDto fetchUser(long userId) {
        UserDto user = userServiceClient.getUser(userId);
        user.setPreference(UserDto.PreferredContact.PHONE);
        user.setId(userId);
        user.setPhone( "1234567890" );
        log.debug("Fetched user: id={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    private void validateUserPreference(UserDto user) {
        if (user.getPreference() == null) {
            log.warn("User {} has no preferred contact method", user.getId());
            throw new NoPreferredContactException(
                    "User " + user.getId() + " has no preferred contact method"
            );
        }
    }

    private NotificationService selectNotificationService(UserDto.PreferredContact preference) {
        return notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(preference))
                .findFirst()
                .orElseThrow(() -> new NotificationServiceNotFoundException(
                        "No NotificationService found for contact type: " + preference
                ));
    }

    private void sendNotification(UserDto user, String message, NotificationService service) {
        log.info("Sending notification to user {} via {}", user.getId(), user.getPreference());
        service.send(user, message);
        log.info("Notification sent successfully to user {}", user.getId());
    }
}