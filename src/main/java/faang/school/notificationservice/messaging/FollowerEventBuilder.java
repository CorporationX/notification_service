package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerEventBuilder implements MessageBuilder<FollowerEvent> {
    private static final String MESSAGE_KEY = "follower.new";
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return FollowerEventBuilder.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        return messageSource.getMessage(MESSAGE_KEY, new Object[]{event.followerName()}, locale);
    }
}
