package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.dto.post.LikeDto;
import faang.school.notificationservice.event.kafka.EventStartNotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static faang.school.notificationservice.util.MessageUtils.truncateContent;

@Component
@RequiredArgsConstructor
public class CommentLikedEventMessageBuilder implements MessageBuilder<AggregatedNotificationsDto> {

    @Value("${message-builder.max-message-length}")
    private int maxMessageLength;

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final PostServiceClient postServiceClient;

    public Class<?> getInstance() {
        return EventStartNotificationEvent.class;
    }

    @Override
    public String buildMessage(AggregatedNotificationsDto notificationsDto, Locale locale) {
        Long count = notificationsDto.getNotificationCount();
        Long commentId = notificationsDto.getTargetEntityId();
        Long likeId = notificationsDto.getRelatedEntityId();

        if (count > 1) {
            return getMultipleLikesMessage(count, commentId, locale);
        }

        return getSingleLikeMessage(likeId, commentId, locale);
    }

    private String getSingleLikeMessage(Long likeId, Long commentId, Locale locale) {
        String messageTemplateCode = "comment.liked.single";
        String content = getCommentContent(commentId);
        UserDto liker = getUser(getLike(likeId).getUserId());

        return messageSource.getMessage(messageTemplateCode,
                new Object[]{truncateContent(content, maxMessageLength), liker.getUsername()},
                locale
        );
    }

    private String getMultipleLikesMessage(Long count, Long commentId, Locale locale) {
        String messageTemplateCode = "comment.liked";
        String content = getCommentContent(commentId);

        return messageSource.getMessage(messageTemplateCode,
                new Object[]{truncateContent(content, maxMessageLength), count},
                locale
        );
    }

    private UserDto getUser(Long userId) {
        return userServiceClient.getUser(userId);
    }

    private LikeDto getLike(Long likeId) {
        return postServiceClient.getLike(likeId);
    }

    private String getCommentContent(Long commentId) {
        return postServiceClient.getComment(commentId).getContent();
    }
}
