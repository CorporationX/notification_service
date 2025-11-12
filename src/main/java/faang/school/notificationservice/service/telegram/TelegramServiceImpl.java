package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramServiceImpl extends TelegramLongPollingBot implements NotificationService{

    public TelegramServiceImpl(@Value("${telegram.token}") String botToken) {
        super(botToken);
        log.info("Telegram bot '{}' initialized", getBotUsername());
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = "Greetings, glad to have joined Corporation X";
            long chatId = update.getMessage().getChatId();
            executeMessage(chatId, messageText);
        }
    }

    private void executeMessage(long chatId, String messageText) {
        if (chatId < 0 && messageText == null) {
            return;
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message via telegram");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        try {
            GetMe getMe = new GetMe();
            User botUser = execute(getMe);
            return botUser.getUserName();
        } catch (TelegramApiException e) {
            log.error("Error getting bot info");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(UserDto dto, String messageText) {
        executeMessage(dto.getId(), messageText);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
