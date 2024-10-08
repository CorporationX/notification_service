package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.LikeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEventDto> {
    private final MessageSource messageSource;
    private UserServiceClient userServiceClient;

    @Override
    public String buildMessage(LikeEventDto event, Locale locale) {
        return messageSource.getMessage("like.new",
                new Object[]{event.likeAuthorId(), event.postId()}, locale);
    }
}
