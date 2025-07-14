package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PostLikedEventMessageBuilder implements MessageBuilder<PostLikedNotificationEvent> {
    private final MessageSource messageSource;

    public Class<?> getInstance() {
        return PostLikedNotificationEvent.class;
    }

    @Override
    public String buildMessage(PostLikedNotificationEvent event, Locale locale) {
        return messageSource.getMessage("post.liked", new Object[]{event.getPostId()}, locale);
    }
}
