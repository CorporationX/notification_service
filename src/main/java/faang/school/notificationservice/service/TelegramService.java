package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegrambot.TelegramBot;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot bot;

    @Override
    public void send(UserDto user, String message) {
        bot.sendNotification(user.getId(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}