package faang.school.notificationservice.service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MyAmazingBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";

    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
