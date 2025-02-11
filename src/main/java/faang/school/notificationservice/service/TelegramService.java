package faang.school.notificationservice.service;

import faang.school.notificationservice.config.TelegramBotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot {
    private final TelegramBotConfig botConfig;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                forwardMessageToTargetChat(text);
            }
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        //return "12345:qwertyuiopASDGFHKMK";
        return botConfig.getToken();
    }

    private void forwardMessageToTargetChat(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(botConfig.getChatId());
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /* @Override
    public void send(UserDto user, String message) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        UserDto userDto = new UserDto();
        return userDto.getPreference();
    } */
}
