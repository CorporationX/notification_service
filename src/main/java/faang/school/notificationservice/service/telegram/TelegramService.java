package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.TELEGRAM;

@Service
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class TelegramService implements NotificationService, CommandLineRunner {

    private TelegramBot bot;

    @Override
    public void run(String... args) {
        try {
            bot = new TelegramBot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(UserDto user, String message) {
        if (user.getPreference() == TELEGRAM) {
            String chatId = String.valueOf(user.getId());
            bot.sendMessage(chatId, message);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return TELEGRAM;
    }
}
