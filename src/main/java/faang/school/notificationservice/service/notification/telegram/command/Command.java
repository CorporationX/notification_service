package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.TelegramProfilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@RequiredArgsConstructor
public abstract class Command {

    protected final MessageSource messageSource;
    protected final Locale defaultLocale = new Locale("ru", "RU");
    protected final TelegramProfilesService telegramProfilesService;
    protected final UserServiceClient userServiceClient;

    abstract SendMessage execute(long chatId, String userName);
}
