package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSender {
    private final List<NotificationService> notificationServices;
    private final UserGetter userGetter;

    public void sendNotification(Long id, String message) {
        UserDto user = userGetter.getUserWithValidate(id);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No notification service found for the user's preferred communication method.")
                )
                .send(user, message);
    }
}