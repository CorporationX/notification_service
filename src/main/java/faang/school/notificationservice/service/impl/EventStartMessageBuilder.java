package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.EventStartEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.List;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventStartEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public Class<EventStartEvent> getSupportedClass() {
        return EventStartEvent.class;
    }

    @Override
    public String buildMessage(EventStartEvent event, Locale locale) {
//        LocalDateTime eventStartDateTime = event.getStartDateTime();
//        String formattedDateTime = eventStartDateTime.format(formatter);
        return messageSource.getMessage("event.start.new",
                new Object[]{"eventStartDateTime"},
                locale);
    }
}
