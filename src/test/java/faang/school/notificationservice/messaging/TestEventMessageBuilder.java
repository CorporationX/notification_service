package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.TestEvent;

import java.util.Locale;

public class TestEventMessageBuilder implements MessageBuilder<TestEvent> {
    @Override
    public Class<?> getInstance() {
        return TestEvent.class;
    }

    @Override
    public String buildMessage(TestEvent event, Locale locale) {
        return "%s in %s".formatted(event.text(), locale.getDisplayName());
    }
}
