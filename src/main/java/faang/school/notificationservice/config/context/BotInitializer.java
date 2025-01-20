package faang.school.notificationservice.config.context;

import faang.school.notificationservice.service.telegram.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {
    private final TelegramBotService bot;
    private static boolean botInitialized = false;

    @EventListener({ContextRefreshedEvent.class})
    public synchronized void init() throws TelegramApiException {
        if (!botInitialized) {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            try {
                telegramBotsApi.registerBot(bot);
                botInitialized = true;
                log.info("Telegram bot successfully registered.");
            } catch (TelegramApiException e) {
                log.error("Error occurred while registering bot: " + e.getMessage());
            }
        }
    }
}