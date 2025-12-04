package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.events.RequestEventDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RequestEventMessageBuilder implements MessageBuilder<RequestEventDto> {

    private final MessageSource messageSource;

    public RequestEventMessageBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Class<?> getInstance() {
        return RequestEventDto.class;
    }

    @Override
    public String buildMessage(RequestEventDto event, Locale locale) {
        return messageSource.getMessage( "request.status.notification.message",
                new Object[]{event.requestId(), event.operationType(), event.status()},
                locale
        );
    }
}