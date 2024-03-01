package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.CommentEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEventDto> {

    private final MessageSource messageSource;

    @Override
    public String getMessage(Locale locale, CommentEventDto event) {
        return messageSource.getMessage("comment.new",
                new Object[]{event.getPostId(), event.getContent()}, locale);
    }
}