package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Slf4j
@Component(value = "/praise")
public class PraiseCommand extends Command {

    public PraiseCommand(MessageSource messageSource,
                         UserServiceClient userServiceClient,
                         TelegramProfileService telegramProfileService) {
        super(messageSource, userServiceClient, telegramProfileService);
    }

    @Override
    public SendMessage sendMessage(long chatId, String userName) {
        log.info("Executing PRAISE command for chatId: {} with userName: {}", chatId, userName);
        String message = messageSource.getMessage("telegram.praise", null, Locale.getDefault());
        return buildMessage(chatId, message);
    }
}
