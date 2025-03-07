package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.UnusedTestEvent;

import java.util.Locale;

public class UnusedTestMessageBuilder implements MessageBuilder<UnusedTestEvent> {
    @Override
    public Class<?> getInstance() {
        return UnusedTestEvent.class;
    }

    @Override
    public String buildMessage(UnusedTestEvent event, Locale locale) {
        return "";
    }
}
