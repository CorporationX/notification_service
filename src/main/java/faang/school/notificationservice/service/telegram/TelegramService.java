package faang.school.notificationservice.service.telegram;

import com.google.common.annotations.VisibleForTesting;
import faang.school.notificationservice.properties.TelegramBotProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationException;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBotProperties telegramBotProperties;
    @VisibleForTesting
    MyTelegramBot bot;

    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        bot = new MyTelegramBot(
                telegramBotProperties.getBotUsername(),
                telegramBotProperties.getBotToken()
        );
        botsApi.registerBot(bot);
    }

    @Override
    public void send(UserDto user, String message) {
        if (user == null || user.getTelegramChatId() == null || user.getTelegramChatId().isBlank()) {
            log.error("User does not have a Telegram ID");
            throw new IllegalArgumentException("User does not have a Telegram ID");
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getTelegramChatId());
        sendMessage.setText(message);

        try {
            bot.execute(sendMessage);
        } catch (Exception e) {
            log.error("Failed to send Telegram message", e);
            throw new NotificationException("Failed to send Telegram message");
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
