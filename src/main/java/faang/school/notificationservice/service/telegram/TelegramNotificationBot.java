package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.property.TelegramBotProperty;
import faang.school.notificationservice.service.telegram.command.CommandBuilder;
import faang.school.notificationservice.service.telegram.command.CommandExecutor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class TelegramNotificationBot extends TelegramLongPollingBot {

    private final TelegramBotProperty telegramBotProperty;
    private final TelegramProfileService telegramProfileService;
    private final CommandExecutor commandExecutor;
    private final CommandBuilder commandBuilder;

    public TelegramNotificationBot(TelegramBotProperty telegramBotProperty,
                                   TelegramProfileService telegramProfileService,
                                   CommandExecutor commandExecutor,
                                   CommandBuilder commandBuilder) {
        super(telegramBotProperty.getToken());
        this.telegramBotProperty = telegramBotProperty;
        this.telegramProfileService = telegramProfileService;
        this.commandExecutor = commandExecutor;
        this.commandBuilder = commandBuilder;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Failed to start TelegramBot: ", e);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperty.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            SendMessage message = commandExecutor.executeCommand(messageText, chatId, userName);
            executeMessage(message);
        }
    }

    public void sendMessage(long userId, String text) {
        TelegramProfile profile = telegramProfileService.findByUserId(userId);
        if (profile.isActive()) {
            SendMessage sendMessage = commandBuilder.buildMessage(profile.getChatId(), text);
            executeMessage(sendMessage);
        } else {
            log.info("Can't send message to TelegramProfile with id={} because profile is inactive", profile.getId());
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
            log.info("Notification successfully sent to chat = {}", message.getChatId());
        } catch (TelegramApiException e) {
            log.error("Failed to send notification to chat: ", e);
            throw new IllegalArgumentException(e);
        }
    }
}
