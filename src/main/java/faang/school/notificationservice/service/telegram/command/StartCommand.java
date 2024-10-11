package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component(value = "/start")
public class StartCommand extends Command {

    public StartCommand(Environment environment) {
        super(environment);
    }

    @Override
    public SendMessage execute(CommandDto commandDto) {
        String greetings = environment.getProperty("telegram.greetings", commandDto.getFirstName());
        SendMessage message = makeMessage(greetings, commandDto.getChatId(), commandDto.getFirstName());
        return message;
    }

    private SendMessage makeMessage(String messageText, long chatId, String firstName) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(String.format(messageText, firstName));
        return sendMessage;
    }
}
