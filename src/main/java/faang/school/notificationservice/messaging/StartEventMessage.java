package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.EventStartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class StartEventMessage implements MessageBuilder<EventStartDto> {
    private static final String CODE_MESSAGE_PROPERTIES = "event.start";
    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventStartDto event, Locale locale) {
        return messageSource.getMessage(
                CODE_MESSAGE_PROPERTIES,
                new Object[]{event.title(), prepareEventMessage(event.eventStart(), locale)},
                locale
        );
    }

    public String prepareEventMessage(EventStart eventStart, Locale locale) {
        return switch (eventStart) {
            case ONE_DAY -> messageSource.getMessage("reminder.one_day", null, locale);
            case FIVE_HOURS -> messageSource.getMessage("reminder.five_hours", null, locale);
            case ONE_HOUR -> messageSource.getMessage("reminder.one_hour", null, locale);
            case TEN_MINUTES -> messageSource.getMessage("reminder.ten_minutes", null, locale);
            default -> messageSource.getMessage("reminder.default", null, locale);
        };
    }

    @Override
    public Class<EventStartDto> getInstance() {
        return EventStartDto.class;
    }
}