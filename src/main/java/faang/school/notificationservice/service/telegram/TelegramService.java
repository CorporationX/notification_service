package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot implements NotificationService {

    private final TelegramConfig telegramConfig;

    @Override
    public void send(UserDto user, String message) {
        sendMessage(user.getId(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    //TODO: Somehow update user in DB to get his correct userId
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    //MIRROR
                    sendMessage(chatId, messageText);
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", it's unicorns tg bot!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Unable to send message. ChatId - {}, text - {}", chatId, textToSend);
        }
    }

    @Override
    public String getBotToken() {
        return telegramConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getName();
    }
}
