package faang.school.notificationservice.message_builder;

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
    public String buildMessage(CommentEventDto eventType, Locale locale) {
        return messageSource.getMessage("comment.new",
                new Object[]{eventType.getPostId(), eventType.getContent()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return CommentEventDto.class;
    }
}