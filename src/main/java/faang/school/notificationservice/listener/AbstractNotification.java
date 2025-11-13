package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationException;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractNotification {
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationService;

    public void sendNotification(Long usersId, String message) {
        UserDto dto = userServiceClient.getUser(usersId);
        if (dto == null) {
            throw new UserNotFoundException("This user does not exist");
        }
        NotificationService preferredService = notificationService.stream()
                .filter(service ->
                        Objects.equals(service.getPreferredContact(), dto.getPreference()))
                .findFirst()
                .orElseThrow(() ->
                        new NotificationException("The user has not specified their preferred notification type"));
        preferredService.send(dto, message);
    }
}