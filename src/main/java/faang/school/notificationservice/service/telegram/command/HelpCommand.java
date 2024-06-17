package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component(value = "/help")
public class HelpCommand extends Command {

    public HelpCommand(MessageSource messageSource,
                       UserServiceClient userServiceClient,
                       TelegramProfileService telegramProfileService,
                       CommandBuilder commandBuilder) {
        super(messageSource, userServiceClient, telegramProfileService, commandBuilder);
    }

    @Override
    public SendMessage sendMessage(long chatId, String userName) {
        log.info("Executing HELP command for chatId: {} with userName: {}", chatId, userName);
        String message = messageSource.getMessage("telegram.help", null, defaultLocale);
        return commandBuilder.buildMessage(chatId, message);
    }
}
