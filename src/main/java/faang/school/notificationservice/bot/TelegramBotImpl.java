package faang.school.notificationservice.bot;

import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBotImpl extends TelegramLongPollingBot {

    private final TelegramBotConfig botConfig;

    public TelegramBotImpl(TelegramBotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("This bot send only notifications from CorpX.");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Failed to send message to Telegram", e);
            }
        }
    }
}