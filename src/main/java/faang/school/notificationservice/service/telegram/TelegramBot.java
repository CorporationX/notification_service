package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramConfig;
import faang.school.notificationservice.exception.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramConfig config;

    public void sendMessage(@NotNull String chatId, @NotNull String text) {
        log.info("The TelegramBot has started processing the message.");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
            log.info("Message delivered.");
        } catch (UserNotFoundException e) {
            log.error("Message not delivered. User to chat id not found.");
            throw new UserNotFoundException(chatId);
        } catch (TelegramApiException e) {
            log.error("Message not delivered. Crash telegram.");
            throw new RuntimeException();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}