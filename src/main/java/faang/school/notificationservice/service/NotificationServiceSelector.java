package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс, который выбирает подходящий сервис для отправки уведомления
 * пользователю на основе его предпочтений.
 */
@Component
@RequiredArgsConstructor
public class NotificationServiceSelector {
    private final List<NotificationService> notificationServices;

    /**
     * Отправляет уведомление пользователю, используя сервис, соответствующий его предпочтениям.
     *
     * @param user    пользователь, которому отправляется уведомление
     * @param message текст уведомления для отправки
     */
    public void notifyUser(UserDto user, String message) {
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() ==
                        user.getPreference())
                .findFirst()
                .ifPresent(service -> service.send(user, message));
    }
}