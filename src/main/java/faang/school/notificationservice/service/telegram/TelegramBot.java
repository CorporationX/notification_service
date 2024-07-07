package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.TelegramChat;
import faang.school.notificationservice.repository.TelegramRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    @Getter
    private final Map<Long, String> state = new HashMap<>();
    @Getter
    private final Map<String, String> userData = new HashMap<>();
    private final UserServiceClient userServiceClient;
    private final TelegramRepository telegramRepository;

    @Async("telegramBotPool")
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMessage(chatId, "Добро пожаловать в CorporationX", "");
            } else if (messageText.equals("/help")) {
                sendMessage(chatId, "Список доступных команд: \n /start - запуск бота\n /help - получить список всех команд" +
                                "\n /authorize - авторизация", "Журнал команд");
            } else if (messageText.equals("/authorize")) {
                sendMessage(chatId, "Введите email: ", "Авторизация");
                state.put(chatId, "EMAIL");
            } else if (state.containsKey(chatId)) {
                if ("EMAIL".equals(state.get(chatId))) {
                    userData.put(chatId + "_email", messageText);
                    sendMessage(chatId, "Введите password: ", "Авторизация");
                    System.out.println(userData);
                    state.put(chatId, "PASSWORD");
                } else if ("PASSWORD".equals(state.get(chatId))) {
                    userData.put(chatId + "_password", messageText);
                    String email = userData.get(chatId + "_email");
                    String password = userData.get(chatId + "_password");
                    Long result = userServiceClient.authorizeUser(email, password);

                    if (result != null) {
                        TelegramChat telegramChat = TelegramChat.builder().chatId(chatId).postAuthorId(result).build();
                        telegramRepository.save(telegramChat);
                        sendMessage(chatId, "Спасибо, вы авторизованы!", "Авторизация");
                    } else {
                        sendMessage(chatId, "Вы не авторизованы!", "Авторизация");
                    }

                    state.remove(chatId);
                }
            } else {
                sendMessage(chatId, "Извините, я не могу дать ответ на ваше сообщение. Вы ввели неизвестную мне команду", "");
            }
        }
    }

    @Async("telegramBotPool")
    public void sendNotification(Long chatId, String message, String messagesHeader) {
        sendMessage(chatId, message, messagesHeader);
    }

    @Override
    public String getBotUsername() {
        return config.botName;
    }

    @Override
    public String getBotToken() {
        return config.botToken;
    }

    public void sendMessage(long chatId, String message, String messagesHeader) {
        SendMessage notification = new SendMessage();
        notification.setChatId(chatId);
        notification.setText(messagesHeader + "\n\n" + message);

        try {
            execute(notification);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
