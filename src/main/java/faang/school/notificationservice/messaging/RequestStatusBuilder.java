package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RequestStatusDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.listener.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_REQUEST_STATUS;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestStatusBuilder implements MessageBuilder<RequestStatusDto> {

    @Value("${spring.messages.property-request-status}")
    private final String requestStatusProperty;
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public EventType getEventType() {
        return EVENT_TYPE_REQUEST_STATUS;
    }

    @Override
    public String buildMessage(RequestStatusDto event, Locale locale) {
        log.info("event status: {}", event.requestStatus());

        UserDto user = userServiceClient.getUser(event.createdBy());
        log.info("getting user: {}", user);
        return messageSource.getMessage(requestStatusProperty,
                new Object[]{user.getUsername(), event.requestStatus()}, locale);
    }
}
