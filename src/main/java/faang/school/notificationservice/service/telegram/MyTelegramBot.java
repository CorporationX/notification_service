package faang.school.notificationservice.service.telegram;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

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
            System.out.println("Telegram Chat ID: " + update.getMessage().getChatId());
        }
    }
}