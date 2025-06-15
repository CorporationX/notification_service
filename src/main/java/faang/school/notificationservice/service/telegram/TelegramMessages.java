package faang.school.notificationservice.service.telegram;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class TelegramMessages {

    private final Map<TelegramLabel, String> messages = new HashMap<>();

    public TelegramMessages(MessageSource messageSource, Locale locale) {
        for (TelegramLabel telegramLabel : TelegramLabel.values()) {
            String label = telegramLabel.getLabel();
            messages.put(telegramLabel, messageSource.getMessage(label, null, locale));
        }
    }

    public String get(TelegramLabel telegramLabel) {
        return messages.get(telegramLabel);
    }
}
