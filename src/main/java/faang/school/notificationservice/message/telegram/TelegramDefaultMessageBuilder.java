package faang.school.notificationservice.message.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TelegramDefaultMessageBuilder {

    private final MessageSource messageSource;

    public String buildMessage(Locale locale) {
        return messageSource.getMessage("telegramDefault.new",null, locale);
    }
}
