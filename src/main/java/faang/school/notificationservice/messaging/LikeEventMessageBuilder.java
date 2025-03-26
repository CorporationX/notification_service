package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.PostShortContentDto;
import faang.school.notificationservice.dto.UserNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventMessageBuilder implements MessageBuilder<LikePostEvent>{
    private final MessageSource messageSource;
    public final UserServiceClient userServiceClient;
    public final PostServiceClient postServiceClient;

    @Override
    public Class<?> getInstance() {
        return LikePostEvent.class;
    }

    @Override
    public String buildMessage(LikePostEvent event, Locale locale) {
        UserNotificationDto userNotificationDtoAuthor = userServiceClient.getNotificationUser(event.getPostAuthorId());
        UserNotificationDto userNotificationDtoUser = userServiceClient.getNotificationUser(event.getLikeUserId());
        PostShortContentDto postShortContentDto = postServiceClient.getShortenedPost(event.getPostId());
        return messageSource.getMessage("postlike.new",
                new Object[] { userNotificationDtoAuthor.getUsername(),
                        userNotificationDtoUser.getUsername(),
                        postShortContentDto.getShortContent()},
                locale);
    }
}
