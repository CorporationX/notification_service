package faang.school.notificationservice.messaging.event;

import faang.school.notificationservice.config.messaging.EventMessagesProperties;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.kafka.event.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventMessageBuilder implements MessageBuilder<EventMessage> {

    private final MessageSource messageSource;
    private final EventMessagesProperties messageProperties;

    @Override
    public Class<EventMessage> getInstance() {
        return EventMessage.class;
    }

    @Override
    public String buildMessage(EventMessage event, Locale locale) {

        Object[] args = new Object[] {
                event.initiatorName(),
                event.title(),
                format(event.startDate()),
                format(event.endDate()),
                event.location()
        };

        String message = messageSource.getMessage(messageProperties.getNewLabel(), args, locale);

        log.info("Message {} was written for the users", message);
        return message;
    }
}
