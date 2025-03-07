package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.TestEvent;

import java.util.Locale;

public class TestMessageBuilder implements MessageBuilder<TestEvent> {

    @Override
    public Class<TestEvent> getInstance() {
        return TestEvent.class;
    }

    @Override
    public String buildMessage(TestEvent event, Locale locale) {
        return "";
    }
}