package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component(value = "/error")
public class ErrorCommand extends Command {
    private static final String COMMAND_NOT_EXIST = "The command does not exist";

    public ErrorCommand(Environment environment) {
        super(environment);
    }

    @Override
    public SendMessage execute(CommandDto commandDto) {
        SendMessage sendMessage = makeMessage(commandDto.getText(), commandDto.getChatId());
        return sendMessage;
    }

    private SendMessage makeMessage(String messageText, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(String.format(COMMAND_NOT_EXIST, messageText));
        return sendMessage;
    }
}
