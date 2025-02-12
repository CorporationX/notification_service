package faang.school.notificationservice.telegram;

import faang.school.notificationservice.config.TelegramBotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static long DUMMY_USER = 0L;
    private final TelegramBotConfig botConfig;

    public TelegramBot(TelegramBotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                sendMessage(DUMMY_USER, text);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    public void sendMessage(long userId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(botConfig.getChatId());
        sendMessage.setText(text);

        try {
            execute(sendMessage);
            log.info("Message successfully sent to user with id {} ", userId);
        } catch (TelegramApiException e) {
            log.error("Unable to send message to user with id {} ", userId);
            throw new RuntimeException(e);
        }
    }
}
