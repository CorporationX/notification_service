package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.LikePostEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikePostMessageBuilder implements MessageBuilder<LikePostEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<LikePostEvent> getSupportedClass() {
        return LikePostEvent.class;
    }

    @Override
    public String buildMessage(LikePostEvent event, Locale locale) {

        Long likeAuthorId = event.getLikeAuthorId();
        UserDto userLikeAuthor = userServiceClient.getUser(likeAuthorId);
        String likeAuthorName = userLikeAuthor.getUsername();

        return messageSource.getMessage(
                "like.post",
                new Object[]{likeAuthorName},
                locale
        );
    }
}
