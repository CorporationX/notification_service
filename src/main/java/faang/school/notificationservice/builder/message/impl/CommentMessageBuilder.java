package faang.school.notificationservice.builder.message.impl;

import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.config.messageSource.MessageKeys;
import faang.school.notificationservice.message.event.CommentEvent;

import faang.school.notificationservice.message.event.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {

    protected final MessageKeys messageKeys;
    protected final MessageSource messageSource;


    @Override
    public String build(CommentEvent event, Locale locale) {
        return messageSource.getMessage(
                messageKeys.getLikePost(),
                new Object[]{event.getCommentAuthorId()},
                locale);
    }
}

