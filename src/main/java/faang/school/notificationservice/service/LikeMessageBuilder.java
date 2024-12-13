package faang.school.notificationservice.service;

import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.PostDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final PostServiceClient postServiceClient;

    @Override
    public Class<?> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        UserDto authorLike = userServiceClient.getUser(event.getAuthorLikeId());
        PostDto post = postServiceClient.getPostById(event.getPostId());
        log.info("LikeMessageBuilder PostDto: {}", post);

        return messageSource.getMessage(
                "like.new",
                new Object[]{authorLike.getUsername(), post.content()},
                locale);
    }
}
