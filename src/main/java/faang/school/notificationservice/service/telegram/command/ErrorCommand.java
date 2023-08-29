package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component(value = "/error")
public class ErrorCommand extends Command {
    private static final String COMMAND_NOT_EXIST = "The transmitted command does not exist: %s";

    public ErrorCommand(TelegramBot receiver, Environment environment) {
        super(receiver, environment);
    }

    @Override
    public void execute(CommandDto commandDto) {
        SendMessage message = initMassage(commandDto.getTextCommand(), commandDto.getChatId());
        try {
            receiver.execute(message);
            log.warn("A non-existent command was transmitted: {} from chat: {}",
                    commandDto.getTextCommand(), commandDto.getChatId());
        } catch (TelegramApiException e) {
            log.error("Failed to send a message about an invalid command to chat: {}", commandDto.getChatId());
            throw new RuntimeException(e);
        }
    }

    private SendMessage initMassage(String messageText, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(String.format(COMMAND_NOT_EXIST, messageText));
        return sendMessage;
    }
}
