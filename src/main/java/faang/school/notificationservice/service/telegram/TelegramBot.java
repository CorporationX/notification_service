package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.BotConfig;
import faang.school.notificationservice.service.telegram.command.CommandExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final char COMMAND_ANNOUNCEMENT = '/';

    private final BotConfig config;
    private final CommandExecutor executor;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getFrom().getFirstName();

            if (checkCommand(messageText)) {
                executor.executeCommand(chatId, firstName, messageText);
            }
        }
    }

    private boolean checkCommand(String messageText) {
        return COMMAND_ANNOUNCEMENT == (messageText.charAt(0));
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
