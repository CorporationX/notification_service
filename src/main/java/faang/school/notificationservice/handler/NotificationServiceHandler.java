package faang.school.notificationservice.handler;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationServiceHandler {
    private final List<NotificationService> notificationServices;

    public void sendSingleNotification(UserServiceDto profileOwner, String message) {
        getNotificationService(profileOwner).send(profileOwner, message);
    }

    public void sendListOfNotification(List<UserServiceDto> profileOwners, String message) {
        profileOwners.forEach(profileOwner -> sendSingleNotification(profileOwner, message));
    }

    private NotificationService getNotificationService(UserServiceDto user) {
        List<NotificationService> list = notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .toList();

        return getSingleNotificationServiceThrow(list, user);
    }

    private NotificationService getSingleNotificationServiceThrow(List<NotificationService> list, UserServiceDto profileOwner) {
        int size = list.size();

        if (size == 0) {
            String error = String.format("No notification service found for %s", profileOwner);
            log.error(error);
            throw new NotFoundElementException(error);
        }

        if (size != 1) {
            String error = String.format("Expected exactly 1 notification service for: %s, but got %d",
                    profileOwner, size);
            log.error(error);
            throw new ListSizeNotOneException(error);
        }

        return list.get(0);
    }
}
