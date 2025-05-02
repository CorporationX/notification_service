package faang.school.notificationservice.messaging.like;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeMessageBuilderImpl implements MessageBuilder<LikeEvent> {

    private static final String LIKE_CODE = "like.post.new";
    private static final String DELETE_LIKE_CODE = "not.like.post";

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<LikeEvent> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        UserDto authorLike;
        try {
            authorLike = userServiceClient.getUser(event.getAuthorLikeId());
            if (authorLike == null) {
                log.warn("User service returned null for user id {}", event.getAuthorLikeId());
                throw new IllegalStateException("User not found: " + event.getAuthorLikeId());
            }
        } catch (Exception e) {
            log.error("Failed to fetch like user with id {}: {}", event.getAuthorLikeId(), e.getMessage(), e);
            throw new IllegalStateException("Failed to fetch like author user: " + event.getAuthorLikeId(), e);
        }

        String code = event.isDeleted() ? DELETE_LIKE_CODE : LIKE_CODE;
        Object[] args = new Object[]{event.getPostId(), authorLike.getUsername()};
        return messageSource.getMessage(code, args, locale);
    }
}

