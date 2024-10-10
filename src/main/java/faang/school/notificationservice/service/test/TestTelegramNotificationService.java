package faang.school.notificationservice.service.test;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestTelegramNotificationService implements NotificationService {
    // Murzin34 Данный класс: заглушка для задачи "Уведомление о получении комментария"
    // При написании нормального сервиса, этот надо удалить
    @Override
    public void send(UserDto user, String message) {
        UserDto.PreferredContact userPreferredContact = user.getPreference();
        log.info("Notification by user preference: " + userPreferredContact);
        log.info("Notification message: " + message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
