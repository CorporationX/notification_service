package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEventDto> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;
    @Override
    public String buildMessage(LikeEventDto event, Locale locale) {
        String followerName = userServiceClient.getUserInternal(event.getAuthorId()).username();
        return messageSource.getMessage("post_like.new", new Object[]{followerName}, locale);
    }
}