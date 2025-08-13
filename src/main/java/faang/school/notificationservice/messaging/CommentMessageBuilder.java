package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.CommentEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * CommentMessageBuilder — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author agent
 * @since 13.08.2025
 */
@Component
public class CommentMessageBuilder implements MessageBuilder<CommentEvent> {
    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Override
    public String buildMessage(CommentEvent event, Locale locale) {
        return "Пользователь " + event.commentAuthorId() +
               " прокомментировал ваш пост " + event.postId() +
               ": \"" + event.text() + "\"";
    }
}