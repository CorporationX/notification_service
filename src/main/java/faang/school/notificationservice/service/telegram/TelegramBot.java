package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUsername;
    private final UserServiceClient userServiceClient;
    private final Map<Long, String> awaitingEmailState = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Failed to register bot");
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                sendMessage(chatId, "Привет! Я бот для уведомлений. Для аутентификации введи свой email.");
                awaitingEmailState.put(chatId, "AWAITING_EMAIL");
                return;
            }
            if (awaitingEmailState.get(chatId) != null) {
                userServiceClient.updateChatIdByEmail(chatId, messageText.trim());
                sendMessage(chatId, "Отлично! Теперь ты сможешь получать уведомления (:");
                awaitingEmailState.remove(chatId);
                return;
            }
            sendMessage(chatId, "Команда не распознана. Введи \"/start\" для начала работы");
        }
    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Message did not send into dialog #{}", chatId);
            throw new RuntimeException(e.getMessage());
        }
    }
}
