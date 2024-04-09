package faang.school.notificationservice.builder;

import faang.school.notificationservice.dto.CreateRequestEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CreateRequestMessageBuilder implements MessageBuilder<CreateRequestEvent>{
    @Override
    public String buildMessage(CreateRequestEvent eventType, Locale locale) {
        return "The status of your request is " + eventType.getRequestStatus();
    }

    @Override
    public Class<?> supportsEventType() {
        return CreateRequestEvent.class;
    }
}
