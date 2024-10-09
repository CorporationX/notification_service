package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.context.BotConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final TelegramBot bot;
    private final BotConfig config;

    @Override
    public void send(UserDto user, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getTelegramChatId());
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
