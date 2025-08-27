package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.EventStartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EventMessageBuilder implements MessageBuilder<EventStartEvent> {
    private final MessageSource messageSource;

    @Autowired
    public EventMessageBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Class<?> getInstance() {
        return EventStartEvent.class;
    }

    @Override
    public String buildMessage(EventStartEvent event, Locale locale) {
        return messageSource.getMessage("event.new", null, Locale.getDefault());
    }
}
