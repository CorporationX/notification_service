package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private final TgNotificationBot tgNotificationBot;
    private final TelegramChatService telegramChatService;

    @Override
    public void send(UserDto user, String message) {
        long chatId = telegramChatService.findChatIdByUserId(user.getId());
        tgNotificationBot.sendTextMessage(chatId, message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}