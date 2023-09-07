package faang.school.notificationservice.command.telegram;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
public abstract class Command {
    private final MessageSource messageSource;

    public abstract SendMessage execute(long chatId, String userName);
}
