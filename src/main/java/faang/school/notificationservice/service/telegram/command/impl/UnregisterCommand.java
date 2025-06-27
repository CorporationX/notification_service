package faang.school.notificationservice.service.telegram.command.impl;

import faang.school.notificationservice.config.TelegramConfig;
import faang.school.notificationservice.service.telegram.command.AbstractTelegramCommand;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;
import java.util.Optional;

@ConditionalOnBean(TelegramConfig.class)
@Component
public class UnregisterCommand extends AbstractTelegramCommand {
    public static final String COMMAND = "/unregister";
    public static final String DESCRIPTION = "unregister.description";

    public UnregisterCommand(
        MessageSource messageSource,
        Locale locale
    ) {
        super(messageSource, locale, COMMAND, DESCRIPTION);
    }

    @Override
    public void execute(Update update) {
        Optional<Long> chatId = getChatId(update);
        chatId.ifPresent(id -> telegramBot.unregisterChatId(id));
    }
}
