package faang.school.notificationservice.service.notification.telegram;

import faang.school.notificationservice.config.telegram.TelegramBotConfiguration;
import faang.school.notificationservice.entity.TelegramProfiles;
import faang.school.notificationservice.service.TelegramProfilesService;
import faang.school.notificationservice.service.notification.telegram.command.CommandHistory;
import jakarta.validation.constraints.NotNull;
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
    private final TelegramProfilesService telegramProfilesService;
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final CommandHistory commandHistory;

    @Autowired
    public TelegramBot(TelegramBotConfiguration telegramBotConfiguration,
                       TelegramProfilesService telegramProfilesService,
                       CommandHistory commandHistory) {
        super(telegramBotConfiguration.getToken());
        this.telegramBotConfiguration = telegramBotConfiguration;
        this.telegramProfilesService = telegramProfilesService;
        this.commandHistory = commandHistory;
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String nickname = update.getMessage().getFrom().getUserName();

            SendMessage execute = commandHistory.execute(command, chatId, nickname);

            executeMessage(execute);
        }
    }

    public void sendMessage(long userId, String message) {
        TelegramProfiles telegramProfiles = telegramProfilesService.findByUserId(userId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramProfiles.getChatId());
        sendMessage.setText(message);

        executeMessage(sendMessage);
    }

    public void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getName();
    }
}
