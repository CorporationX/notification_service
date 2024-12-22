package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.context.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotService extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    public void sendMessage(Long userId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(userId.toString());
        message.setText(messageText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("An error occurred sending a message", e);
        }
    }
}