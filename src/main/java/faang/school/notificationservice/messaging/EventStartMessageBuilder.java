package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.EventStartEvent;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class EventStartMessageBuilder implements MessageBuilder<EventStartEvent>{

    private MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return EventStartEvent.class;
    }

    @Override
    public String buildMessage(EventStartEvent event, Locale locale, Object[] args) {
        return messageSource.getMessage("start.event", args, locale);
    }
}
