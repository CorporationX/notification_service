package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void send(UserDto user, String message) {
        if (user.getChatId() == null) {
            throw new NullPointerException("User must be registered in TelegramBot");
        }
        telegramBot.sendMessage(user.getChatId(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
