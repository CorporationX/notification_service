package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.TestEvent;

import java.util.Locale;

public class HelloWorldEventMessageBuilder implements MessageBuilder<TestEvent> {
    @Override
    public Class<?> getInstance() {
        return TestEvent.class;
    }

    @Override
    public String buildMessage(TestEvent event, Locale locale) {
        return "Hello World";
    }
}
