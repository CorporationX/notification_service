package faang.school.notificationservice.message.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TelegramWelcomeMessageBuilder {

    private final MessageSource messageSource;

    public String buildMessage(Locale locale, String userName) {
        return messageSource.getMessage("telegramWelcome.new", new Object[]{userName}, locale);
    }
}
