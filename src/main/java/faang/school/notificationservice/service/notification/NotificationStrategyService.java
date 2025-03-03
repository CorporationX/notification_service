package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.BusinessException;
import faang.school.notificationservice.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationStrategyService {
    private final List<NotificationService> notificationServices;

    public NotificationService getNotificationService(UserDto userDto) {
        validateUserPreferredContact(userDto);

        return notificationServices.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new BusinessException("Не найден необходимый сервис отправки уведомления"));
    }

    private void validateUserPreferredContact(UserDto userDto) {
        if (userDto.getPreference() == null) {
            throw new DataValidationException("У пользователя c ID " + userDto.getId()
                    + " отсутствует способ отправки уведомления");
        }
    }
}
