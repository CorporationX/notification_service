package faang.school.notificationservice.service.notification.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@RequiredArgsConstructor
public abstract class Command {
    protected final Locale locale = Locale.US;
    protected final MessageSource messageSource;

    public abstract SendMessage process(long chatId, String text);

    public abstract boolean isApplicable(String textCommand);

    protected SendMessage buildSendMessage(long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    protected String getMessage(String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey, args, this.locale);
    }
}
