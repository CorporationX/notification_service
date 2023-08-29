package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component(value = "/start")
public class StartCommand extends Command {
    private static final String START_ERROR =
            "An error occurred while trying to send a message to chat = {} to user = {}";

    public StartCommand(TelegramBot sender, Environment environment) {
        super(sender, environment);
    }

    @Override
    public void execute(CommandDto commandDto) {
        String greetings = environment.getProperty("telegram.greetings");
        SendMessage message = initMassage(greetings, commandDto.getChatId(), commandDto.getFirstName());
        try {
            receiver.execute(message);
            log.info("Reply sent to chat = {}", message.getChatId());
        } catch (TelegramApiException e) {
            log.error(START_ERROR, commandDto.getChatId(), commandDto.getFirstName(), e);
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
