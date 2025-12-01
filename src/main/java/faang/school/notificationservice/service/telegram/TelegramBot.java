package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
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
    @Value("${telegram.bot.cleanup.state.fixed-rate}")
    private int fixedRateCleanup;
    private final UserServiceClient userServiceClient;
    private final Map<Long, Instant> awaitingEmailState = new ConcurrentHashMap<>();

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
            String messageText = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                sendMessage(chatId, "Привет! Я бот для уведомлений. Для аутентификации введи свой email.");
                awaitingEmailState.put(chatId, Instant.now());
                return;
            }
            if (awaitingEmailState.containsKey(chatId)) {
                try {
                    userServiceClient.updateChatIdByEmail(chatId, messageText);
                    sendMessage(chatId, "Отлично! Теперь ты сможешь получать уведомления (:");
                    awaitingEmailState.remove(chatId);
                    log.info("User with chatId #{} attach telegram-notifications", chatId);
                } catch (Exception e) {
                    sendMessage(chatId, "Упс, что-то пошло не так ):\nПопробуй отправить свой email ещё раз");
                    log.error("User with chatId #{} could not attach telegram-notifications. Error: {}",
                            chatId, e.getMessage());
                }
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
            log.error("Message was not sent into dialog #{}", chatId);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Scheduled(fixedRateString = "${telegram.bot.cleanup.state.fixed-rate}")
    public void cleanupAwaitingEmailState() {
        Instant currentTime = Instant.now();
        for (Map.Entry<Long, Instant> entry : awaitingEmailState.entrySet()) {
            awaitingEmailState.computeIfPresent(entry.getKey(), (chatId, state) -> {
                if (currentTime.isAfter(state.plusMillis(fixedRateCleanup))) {
                    awaitingEmailState.remove(chatId);
                    sendMessage(chatId,
                            "Время ожидания твоего email истекло. Введи \"/start\" для начала работы");
                }
                return null;
            });
        }
    }
}
