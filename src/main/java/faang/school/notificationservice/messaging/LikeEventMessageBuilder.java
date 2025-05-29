package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.LikeEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LikeEventMessageBuilder extends MessageBuilder<LikeEvent> {

    private static final String MESSAGE_KEY = "notification.like";

    public LikeEventMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return buildMessage(MESSAGE_KEY, locale,
                String.valueOf(event.getUserId()), // кто лайкнул
                String.valueOf(event.getPostId())); // пост
    }
}
