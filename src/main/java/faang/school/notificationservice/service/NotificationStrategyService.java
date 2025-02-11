package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationStrategyService {
    private final List<NotificationService> notificationServices;

    public NotificationService getNotificationService(UserDto userDto) {
        return notificationServices.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Не найден способ отправки уведомления"));
    }
}
