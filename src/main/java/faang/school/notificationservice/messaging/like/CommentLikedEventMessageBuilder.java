package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.service.notification.EventType;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class CommentLikedEventMessageBuilder extends LikeNotificationsMessageBuilder {

    public CommentLikedEventMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Class<?> getInstance() {
        return CommentLikedEventMessageBuilder.class;
    }

    @Override
    public String getMessageTemplateCode(EventType eventType, int count) {
        if (count > 1) {
            return "comment.liked";
        }

        return "comment.liked.single";
    }
}
