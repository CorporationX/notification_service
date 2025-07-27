package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.RequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RequestEventMessageBuilder implements MessageBuilder<RequestEvent>{
    private final MessageSource source;

    @Override
    public Class<RequestEvent> supportsEventType() {
        return RequestEvent.class;
    }

    @Override
    public String buildMessage(RequestEvent event, Locale locale) {
        return source.getMessage("notification.request.statusChanged",
                new Object[]{event.getIdempotencyKey(), event.getCurrentStatus()},
                locale);
    }
}
