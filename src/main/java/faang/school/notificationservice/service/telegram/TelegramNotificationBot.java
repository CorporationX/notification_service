package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.property.TelegramBotProperty;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class TelegramNotificationBot extends TelegramLongPollingBot {

    private final TelegramBotProperty telegramBotProperty;

    public TelegramNotificationBot(TelegramBotProperty telegramBotProperty) {
        super(telegramBotProperty.getToken());
        this.telegramBotProperty = telegramBotProperty;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperty.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {}

    public void sendMessage(Long chatId, String textToSend) {

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .build();

        try {
            execute(message);
            log.info("Notification successfully sent to chat = {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Failed to send notification to chat = {}", chatId);
            throw new IllegalArgumentException(e);
        }
    }
}
