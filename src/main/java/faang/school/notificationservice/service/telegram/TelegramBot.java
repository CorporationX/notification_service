package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.BotConfig;
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
public class TelegramBot extends TelegramLongPollingBot {
    private static final String GREETINGS = "Hello %s! It's CorporationX telegram bot!";
    private final BotConfig config;

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getFrom().getFirstName();

            executeCommand(messageText, chatId, firstName);
        }
    }

    private void executeCommand(String messageText, long chatId, String firstName) {
        if (messageText.equals("/start")) {
            startBot(chatId, firstName);
        } else {
            log.info("Unexpected message");
        }
    }

    private void startBot(long chatId, String firstName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format(GREETINGS, firstName));

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
