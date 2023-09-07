package faang.school.notificationservice.command.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
public abstract class Command {
    protected final MessageSource messageSource;

    public abstract SendMessage execute(long chatId, String userName);
}
