package faang.school.notificationservice.command.telegram;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@AllArgsConstructor
public abstract class Command {
    protected static final Locale LOCALE_DEFAULT = Locale.getDefault();
    protected final MessageSource messageSource;

    public abstract SendMessage execute(long chatId, String userName);
}
