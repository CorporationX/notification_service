package faang.school.notificationservice.messaging.builders;

import faang.school.notificationservice.events.NewFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;

import java.util.Locale;

public class NewFollowerMessageBuilder implements MessageBuilder<NewFollowerEvent> {
    @Override
    public Class<?> getInstance() {
        return NewFollowerEvent.class;
    }

    @Override
    public String buildMessage(NewFollowerEvent event, Locale locale) {
        return "ru".equals(locale.getLanguage())
                ? "У вас новый подписчик: " + event.getFollowerDisplayName()
                : "You have a new follower: " + event.getFollowerDisplayName();
    }
}
