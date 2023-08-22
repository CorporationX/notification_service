package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.EventAttendeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventAttendeeDto, Locale> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventAttendeeDto context, Locale locale) {
        return messageSource.getMessage(
                "event.start",
                new Object[]{context.getUsername(), context.getEventTitle()},
                locale);
    }
}
