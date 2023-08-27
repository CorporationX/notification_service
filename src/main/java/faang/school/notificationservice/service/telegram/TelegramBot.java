package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.BotConfig;
import faang.school.notificationservice.service.telegram.command.Command;
import faang.school.notificationservice.service.telegram.command.CommandExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final String START_ERROR =
            "An error occurred while trying to send a message to chat = {} to user = {}";
    private static final char COMMAND_ANNOUNCEMENT = '/';

    private final BotConfig config;
    private final Environment environment;
    private final CommandExecutor executor;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getFrom().getFirstName();

            if (checkCommand(messageText)) {
                executor.executeCommand(messageText, chatId, firstName);
            }
        }
    }

    private void startBot(long chatId, String firstName) {
        SendMessage message = new SendMessage();
        String greetings = environment.getProperty("telegram.greetings");
        message.setChatId(chatId);
        message.setText(String.format(greetings, firstName));

        try {
            execute(message);
            log.info("Reply sent to chat = {}", chatId);
        } catch (TelegramApiException e) {
            log.error(START_ERROR, chatId, firstName, e);
            throw new IllegalArgumentException(e);
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

    public long getChatId() {
        return config.getChatId();
    }
}
