package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.dto.post.LikeDto;
import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static faang.school.notificationservice.util.MessageUtils.truncateContent;

@Component
@RequiredArgsConstructor
public class PostLikedEventMessageBuilder implements MessageBuilder<AggregatedNotificationsDto> {

    @Value("${message-builder.max-message-length}")
    private int maxMessageLength;

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final PostServiceClient postServiceClient;

    public Class<?> getInstance() {
        return PostLikedNotificationEvent.class;
    }

    @Override
    public String buildMessage(AggregatedNotificationsDto notificationsDto, Locale locale) {
        Long count = notificationsDto.getNotificationCount();
        Long postId = notificationsDto.getTargetEntityId();
        Long likeId = notificationsDto.getRelatedEntityId();

        if (count > 1) {
            return getMultipleLikesMessage(count, postId, locale);
        }

        return getSingleLikeMessage(likeId, postId, locale);
    }

    private String getSingleLikeMessage(Long likeId, Long postId, Locale locale) {
        String messageTemplateCode = "post.liked.single";
        String content = getPostContent(postId);
        String likerUsername = getUser(getLike(likeId).getUserId())
                .getUsername();

        return messageSource.getMessage(messageTemplateCode,
                new Object[]{truncateContent(content, maxMessageLength), likerUsername},
                locale
        );
    }

    private String getMultipleLikesMessage(Long count, Long postId, Locale locale) {
        String messageTemplateCode = "post.liked";
        String content = getPostContent(postId);

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

    private String getPostContent(Long postId) {
        return postServiceClient.getPost(postId).getContent();
    }
}