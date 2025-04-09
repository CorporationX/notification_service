package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationServiceSelector {
    private final List<NotificationService> notificationServices;

    public void notifyUser(UserDto user, String message) {
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() ==
                        user.getPreference())
                .findFirst()
                .ifPresent(service -> service.send(user, message));
    }
}