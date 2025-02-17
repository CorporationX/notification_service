package faang.school.notificationservice.exception.handler;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.utils.ListUtil;
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
    private final ListUtil listUtil;

    public void sendSingleNotification(UserServiceDto profileOwner, String message) {
        getNotificationService(profileOwner).send(profileOwner, message);
    }

    public void sendListOfNotification(List<UserServiceDto> profileOwners, String message) {
        profileOwners.forEach(profileOwner -> sendSingleNotification(profileOwner, message));
    }

    private NotificationService getNotificationService(UserServiceDto user) {
        return listUtil.requireSingleElement(
                notificationServices.stream()
                        .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                        .toList(),
                NotificationService.class, UserServiceDto.class);
    }
}
