package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.TelegramConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ConditionalOnBean(TelegramConfig.class)
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
