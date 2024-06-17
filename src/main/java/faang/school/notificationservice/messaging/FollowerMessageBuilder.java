package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.FollowerEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {
    private MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        return messageSource.getMessage("follower.new", new Object[]{}, locale);
    }
}
