package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class StartCommand extends Command {
    public StartCommand(CommandExecutor receiver, TelegramBot sender) {
        super("/start", receiver, sender);
    }

    @Override
    void execute(SendMessage message) {
        String greetings = environment.getProperty("telegram.greetings");

        try {
            receiver.execute(message);
            log.info("Reply sent to chat = {}", message.getChatId());
        } catch (TelegramApiException e) {
            log.error(START_ERROR, chatId, firstName, e);
            throw new IllegalArgumentException(e);
        }
    }
}
