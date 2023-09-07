package faang.school.notificationservice.command.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component(value = "/error")
public class ErrorCommand extends Command {

    @Autowired
    public ErrorCommand(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public SendMessage execute(long chatId, String userName) {
        String response = messageSource.getMessage("telegram.error", null, LOCALE_DEFAULT);

        return buildMessage(chatId, response);
    }

    private SendMessage buildMessage(long chatId, String response) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(response);

        return message;
    }
}
