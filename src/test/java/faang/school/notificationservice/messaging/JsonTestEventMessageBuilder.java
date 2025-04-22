package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.event.AnotherTestEvent;

import java.util.Locale;

public class JsonTestEventMessageBuilder implements MessageBuilder<AnotherTestEvent> {
    private final ObjectMapper objectMapper;

    public JsonTestEventMessageBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<?> getInstance() {
        return AnotherTestEvent.class;
    }

    @Override
    public String buildMessage(AnotherTestEvent event, Locale locale) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }
}
