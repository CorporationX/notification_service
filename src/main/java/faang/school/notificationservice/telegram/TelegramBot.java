package faang.school.notificationservice.telegram;

import faang.school.notificationservice.exception.TelegramInteractionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram-bot.name}")
    private String botName;

    public TelegramBot(@Value("${telegram-bot.token}")
                       String apiToken) {
        super(apiToken);
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new TelegramInteractionException(e.getMessage());
        }
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new TelegramInteractionException(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramInteractionException(e.getMessage());
        }
    }
}
