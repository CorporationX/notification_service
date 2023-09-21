package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.service.telegram.command.CommandExecutor;
import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import faang.school.notificationservice.entity.TelegramProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramProfileService telegramProfileService;
    private final TelegramBotConfig config;
    private final CommandExecutor commandExecutor;

    @Autowired
    public TelegramBot(TelegramProfileService telegramProfileService,
                       TelegramBotConfig config,
                       CommandExecutor commandExecutor) {
        super(config.getToken());
        this.telegramProfileService = telegramProfileService;
        this.config = config;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            SendMessage message = commandExecutor.executeCommand(chatId, userName, messageText);

            sendMessage(message);
        }
    }

    public void sendNotification(Long userId, String message) {
        TelegramProfile profile = telegramProfileService.getByUserId(userId);

        SendMessage notification = new SendMessage();
        notification.setChatId(profile.getChatId());
        notification.setText(message);

        sendMessage(notification);
    }

    private void sendMessage(SendMessage notification) {
        try {
            execute(notification);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
