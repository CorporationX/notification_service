package faang.school.notificationservice.service.telegram;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class MyTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;

    public MyTelegramBot(String botUsername, String botToken) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            log.info("Telegram Chat ID: " + update.getMessage().getChatId());
        }
    }
}