package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.service.telegram.command.Command;
import faang.school.notificationservice.service.telegram.command.CommandExecutor;
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
    private final TelegramBotConfig telegramBotConfig;
    private final CommandExecutor commandExecutor;
    private final Command commandBuildMessage;

    @Autowired
    public TelegramBot(TelegramProfileService telegramProfileService,
                       TelegramBotConfig telegramBotConfig,
                       CommandExecutor commandExecutor,
                       Command command) {
        super(telegramBotConfig.getBotToken());
        this.telegramProfileService = telegramProfileService;
        this.telegramBotConfig = telegramBotConfig;
        this.commandExecutor = commandExecutor;
        this.commandBuildMessage = command;
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            SendMessage message = commandExecutor.execute(messageText, chatId, userName);
            executeMessage(message);
        }
    }

    public void toSendMessage(long userId, String message) {
        TelegramProfile profile = telegramProfileService.findByUserId(userId);
        commandBuildMessage.buildMessage(profile.getChatId(), message);
    }

    private void executeMessage (SendMessage sendMessage) {
        try {
            execute(sendMessage);
            log.info("Message {} has been sent to chatId {}", sendMessage.getText(), sendMessage.getChatId());
        } catch (TelegramApiException exception) {
            log.error(exception.getMessage());
        }
    }
}