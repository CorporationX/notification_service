package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class StartCommand extends Command {
    private static final String START_ERROR =
            "An error occurred while trying to send a message to chat = {} to user = {}";

    public StartCommand(TelegramBot sender, Environment environment) {
        super("/start", sender, environment);
    }

    @Override
    public void execute(long chatId, String firstName) {
        String greetings = environment.getProperty("telegram.greetings");
        SendMessage message = initMassage(greetings, chatId, firstName);
        try {
            receiver.execute(message);
            log.info("Reply sent to chat = {}", message.getChatId());
        } catch (TelegramApiException e) {
            log.error(START_ERROR, chatId, firstName, e);
            throw new IllegalArgumentException(e);
        }
    }

    private SendMessage initMassage(String messageText, long chatId, String firstName) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(String.format(messageText, firstName));
        sendMessage.setChatId(chatId);
        return sendMessage;
    }
}
