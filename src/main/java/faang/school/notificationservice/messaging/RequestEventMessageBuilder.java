package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.events.RequestEventDto;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RequestEventMessageBuilder implements MessageBuilder<RequestEventDto> {

    @Override
    public Class<?> getInstance() {
        return RequestEventDto.class;
    }

    @Override
    public String buildMessage(RequestEventDto event, Locale locale) {
        return String.format(
                "Request %s: Operation %s completed with status %s",
                event.requestId(),
                event.operationType(),
                event.status()
        );
    }
}