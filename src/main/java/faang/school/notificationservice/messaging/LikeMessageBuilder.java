package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.dto.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return CommentEventDto.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return messageSource.getMessage("like.new",
                new Object[]{userServiceClient.getUser(event.getPostAuthorId()).getUsername()}, locale);
    }


}