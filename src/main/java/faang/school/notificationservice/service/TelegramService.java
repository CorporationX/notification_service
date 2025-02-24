package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegram.TelegramBot;
import faang.school.notificationservice.dto.UserNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final TelegramBot telegramBot;
    @Override
    public void send(UserNotificationDto user, String message) {
        telegramBot.sendMessage(user.getChatId(), message);
    }

    @Override
    public UserNotificationDto.PreferredContact getPreferredContact() {
        return UserNotificationDto.PreferredContact.TELEGRAM;
    }
}
