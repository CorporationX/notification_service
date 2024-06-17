package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.FollowerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final MessageSource messageSource;

    @Autowired
    public FollowerMessageBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        return messageSource.getMessage("follower.new", new Object[]{}, locale);
    }
}
