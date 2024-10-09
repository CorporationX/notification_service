package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.context.BotConfig;
import faang.school.notificationservice.service.telegram.command.CommandExecutor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final char COMMAND_ANNOUNCE = '/';

    private final BotConfig config;
    private final CommandExecutor commandExecutor;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getFrom().getFirstName();

            if (checkMessage(messageText)) {
                SendMessage sendMessage = commandExecutor.executeCommand(chatId, firstName, messageText);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private boolean checkMessage(String messageText) {
        return COMMAND_ANNOUNCE == messageText.charAt(0);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
