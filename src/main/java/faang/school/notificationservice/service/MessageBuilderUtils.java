package faang.school.notificationservice.service;

import faang.school.notificationservice.exception.MessageBuilderException;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class MessageBuilderUtils<T> {
    private final List<MessageBuilder<T>> messageBuilders;

    public String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().isAssignableFrom(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new MessageBuilderException("There is no suitable builder for this class, %s"
                        .formatted(event.getClass())))
                .buildMessage(event, locale);
    }
}