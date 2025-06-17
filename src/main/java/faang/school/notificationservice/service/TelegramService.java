package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.NotificationBot;
import faang.school.notificationservice.service.telegram.NotificationFailedException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramService implements NotificationService {
    private final NotificationBot notificationBot;

    public TelegramService(NotificationBot notificationBot) {
        this.notificationBot = notificationBot;
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void send(UserDto user, String message) {
        if (user == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }

        try {
            // Поскольку в UserDto нет telegramChatId, используем id пользователя как chatId
            // В реальном приложении нужно либо добавить это поле в UserDto,
            // либо получить chatId из другого источника (например, базы данных)
            long telegramChatId = user.getId();
            notificationBot.sendMessage(telegramChatId, message);
        } catch (TelegramApiException e) {
            throw new NotificationFailedException(
                    String.format("Failed to send Telegram notification to user %d", user.getId()),
                    e
            );
        }
    }
}