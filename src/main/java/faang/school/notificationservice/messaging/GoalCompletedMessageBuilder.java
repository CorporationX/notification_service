package faang.school.notificationservice.messaging;

import java.util.Locale;

public class GoalCompletedMessageBuilder implements MessageBuilder {
    @Override
    public Class<?> getInstance() {
        return GoalCompletedMessageBuilder.class;
    }

    @Override
    public String buildMessage(Object event, Locale locale) {
        return "";
    }
}
