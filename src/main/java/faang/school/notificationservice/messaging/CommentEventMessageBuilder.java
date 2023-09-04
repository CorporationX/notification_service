package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.CommentEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventMessageBuilder implements MessageBuilder<CommentEventDto> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;


    @Override
    public Class<CommentEventDto> getInstance() {
        return CommentEventDto.class;
    }

    @Override
    public String buildMessage(CommentEventDto event, Locale locale) {
        return messageSource.getMessage(
                "comment.start", new Object[]{
                        userServiceClient.getUser(event.getPostAuthorId()).getUsername(), event.getPostId(), event.getContentComment(),
                        userServiceClient.getUser(event.getAuthorIdComment()).getUsername()}, locale
        );
    }
}
