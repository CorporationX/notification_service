package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component(value = "/unknown")
public class UnknownCommand extends Command {

    @Autowired
    public UnknownCommand(MessageSource messageSource,
                          TelegramProfileService telegramProfileService,
                          UserServiceClient userServiceClient) {
        super(messageSource, telegramProfileService, userServiceClient);
    }

    @Override
    public SendMessage execute(long chatId, String userName) {
        log.info("Received an unknown command from user: {}", userName);

        String response = messageSource.getMessage("telegram.unknown", null, LOCALE_DEFAULT);

        log.debug("Response message for chatId {}: {}", chatId, response);

        return buildMessage(chatId, response);
    }

    private SendMessage buildMessage(long chatId, String response) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(response);

        return message;
    }
}
