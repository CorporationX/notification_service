package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.service.notification.EventType;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class PostLikedEventMessageBuilder extends LikeNotificationsMessageBuilder {

    public PostLikedEventMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    public Class<?> getInstance() {
        return PostLikedNotificationEvent.class;
    }

    @Override
    public String getMessageTemplateCode(EventType eventType, int count) {
        if (count > 1) {
            return "post.liked";
        }

        return "post.liked.single";
    }
}