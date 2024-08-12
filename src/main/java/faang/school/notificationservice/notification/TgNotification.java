package faang.school.notificationservice.notification;

import faang.school.notificationservice.config.context.TgNotificationBot;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TgNotification implements NotificationService {
    private final TgNotificationBot tgNotificationBot;
    private final TelegramChatService telegramChatService;

    @Override
    public void send(UserDto user, String message) {
        Long chatId = telegramChatService.getChatId(user.getId());
        tgNotificationBot.sendTextMessage(chatId, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
