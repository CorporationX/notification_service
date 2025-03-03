package faang.school.notificationservice.service.bot;

import faang.school.notificationservice.config.telegram.BotProperties;
import faang.school.notificationservice.exception.IntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final BotProperties botProperties;
    private final TelegramClient telegramClient;

    public void sendMessage(long telegramChatId, String notificationMessage) {
        SendMessage message = SendMessage.builder()
                .chatId(telegramChatId)
                .text(notificationMessage)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            String errorMessage = "Отправка уведомления через телеграм для chat id " + telegramChatId +
                    " завершилась с ошибкой: " + e.getMessage();
            log.error(errorMessage, e);
            throw new IntegrationException(errorMessage);
        }
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();

            log.info("Telegram chat id = {}", chatId);
        }
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Зарегистрированный бот с именем {} имеет статус работы: {}",
                botProperties.getName(), botSession.isRunning());
    }
}
