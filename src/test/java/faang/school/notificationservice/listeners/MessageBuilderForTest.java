package faang.school.notificationservice.listeners;

import faang.school.notificationservice.messaging.MessageBuilder;

import java.util.Locale;

public class MessageBuilderForTest implements MessageBuilder<EventForTest> {

    @Override
    public Class<EventForTest> getInstance() {
        return EventForTest.class;
    }

    @Override
    public String buildMessage(EventForTest event, Locale locale) {
        return "test_message";
    }
}
