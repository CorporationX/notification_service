package faang.school.notificationservice.service.message_builder;

import faang.school.notificationservice.dto.CommentEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEventDto> {

    @Autowired
    private final MessageSource messageSource;

    @Override
    public String buildMessage(CommentEventDto event, Locale locale) {
        return messageSource.getMessage("comment.new",
                new Object[]{event.getPostId(), event.getContent()}, locale);
    }

    @Override
    public long getReceiverId(CommentEventDto event) {
        return event.getAuthorPostId();
    }
}