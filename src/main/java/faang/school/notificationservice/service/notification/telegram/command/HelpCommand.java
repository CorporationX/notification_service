package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.TelegramProfilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component("/help")
public class HelpCommand extends Command {

    public HelpCommand(MessageSource messageSource, TelegramProfilesService telegramProfilesService, UserServiceClient userServiceClient) {
        super(messageSource, telegramProfilesService, userServiceClient);
    }

    @Override
    public SendMessage execute(long chatId, String userName) {
        log.info("Executing helpCommand");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message = messageSource.getMessage("telegram.help", new Object[]{userName}, defaultLocale);
        sendMessage.setText(message);
        return sendMessage;
    }
}
