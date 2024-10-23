package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.config.message.BuilderProperties;
import faang.school.notificationservice.dto.comment.NewCommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class NewCommentEventBuilder implements MessageBuilder<NewCommentEvent> {

    private final BuilderProperties builderProperties;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return NewCommentEvent.class;
    }

    @Override
    public String buildMessage(NewCommentEvent event, Locale locale) {
        String content = event.getContent();
        if (content.length() > builderProperties.getNewCommentEventOpt().getStartMessageIndex()) {
            content = content.substring(builderProperties.getNewCommentEventOpt().getStartMessageIndex(),
                    builderProperties.getNewCommentEventOpt().getLastMessageIndex());
        }

        return messageSource.getMessage("comment.new", new Object[]{content}, locale);
    }
}
