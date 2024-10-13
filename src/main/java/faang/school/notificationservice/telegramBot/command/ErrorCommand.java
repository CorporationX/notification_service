package faang.school.notificationservice.telegramBot.command;

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
        return createMessage(commandDto.getText(), commandDto.getChatId());
    }

    private SendMessage createMessage(String messageText, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(String.format("The command %s does not exist", messageText));
        return sendMessage;
    }
}
