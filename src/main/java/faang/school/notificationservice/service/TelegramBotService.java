package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class TelegramBotService implements LongPollingSingleThreadUpdateConsumer, NotificationService {
    private final TelegramClient telegramClient;
    private final TelegramBotsLongPollingApplication botsApplication;
    private final String botToken;

    public TelegramBotService(@Value("${telegram.bot.token}") String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.botsApplication = new TelegramBotsLongPollingApplication();
    }

    @PostConstruct
    public void init() {
        try {
            botsApplication.registerBot(botToken, this);
            log.info("Telegram bot successfully started!");
        } catch (Exception e) {
            log.error("Failed to start Telegram bot", e);
        }
    }

    @Override
    public void consume(Update update) {
        System.out.println("Получено обновление: " + update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            log.info("Сообщение от пользователя: " + messageText);

        }
    }

    @Override
    public void send(UserDto user, String message) {
        SendMessage telegramMessage = SendMessage.builder()
                .chatId(user.getTelegramChatId())
                .text(message)
                .build();

        try {
            telegramClient.execute(telegramMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

}
