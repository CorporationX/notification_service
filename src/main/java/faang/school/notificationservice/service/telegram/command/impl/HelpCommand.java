package faang.school.notificationservice.service.telegram.command.impl;

import faang.school.notificationservice.service.telegram.command.AbstractTelegramCommand;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;
import java.util.Optional;

@Component
public class HelpCommand extends AbstractTelegramCommand {
    public static final String COMMAND = "/help";
    public static final String DESCRIPTION = "help.description";

    public HelpCommand(
            MessageSource messageSource,
            Locale locale
    ) {
        super(messageSource, locale, COMMAND, DESCRIPTION);
    }

    @Override
    public void execute(Update update) {
        Optional<Long> chatId = getChatId(update);
        chatId.ifPresent(id -> telegramBot.sendCommandList(id));
    }
}
