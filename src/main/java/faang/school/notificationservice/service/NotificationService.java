package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface NotificationService {

    void send(UserDto user, String message) throws TelegramApiException;

    UserDto.PreferredContact getPreferredContact();
}
