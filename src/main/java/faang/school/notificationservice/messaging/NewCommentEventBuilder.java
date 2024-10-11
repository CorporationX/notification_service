package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.comment.NewCommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class NewCommentEventBuilder implements MessageBuilder<NewCommentEvent> {

    private static final int START_MESSAGE_INDEX = 0;
    private static final int FINISH_MESSAGE_INDEX = 20;

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return NewCommentEvent.class;
    }

    @Override
    public String buildMessage(NewCommentEvent event, Locale locale) {
        String content = event.getContent();
        if (content.length() > FINISH_MESSAGE_INDEX) {
            content = content.substring(START_MESSAGE_INDEX, FINISH_MESSAGE_INDEX);
        }

        return messageSource.getMessage("comment.new", new Object[]{content}, locale);
    }
}
