package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class NewCommentMessageBuilder implements MessageBuilder<CommentEventDto> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(CommentEventDto event, Locale locale) {
        String commentatorName = userServiceClient.getUserInternal(event.getAuthorCommentId()).username();

        return messageSource.getMessage("comment.new",
                new Object[]{commentatorName, event.getContent()},
                locale);
    }
}
