package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.TelegramProfilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component("/unknown")
public class UnknownCommand extends Command {

    public UnknownCommand(MessageSource messageSource, TelegramProfilesService telegramProfilesService, UserServiceClient userServiceClient) {
        super(messageSource, telegramProfilesService, userServiceClient);
    }

    @Override
    public SendMessage execute(long chatId, String nickname) {
        log.info("Executing unknownCommand with chatId {} and nickname {}", chatId, nickname);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message = messageSource.getMessage("telegram.unknown", null, defaultLocale);
        sendMessage.setText(message);
        return sendMessage;
    }
}
