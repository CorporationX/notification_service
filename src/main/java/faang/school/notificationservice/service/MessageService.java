package faang.school.notificationservice.service;

import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final List<MessageBuilder<?>> messageBuilders;

    public <T> String buildMessage(Class<T> eventType, T event, Locale locale) {
        MessageBuilder<T> builder = findMessageBuilder(eventType);
        String message = builder.buildMessage(event, locale);
        log.debug("Built message for event type {}", eventType.getSimpleName());
        return message;
    }

    @SuppressWarnings("unchecked")
    private <T> MessageBuilder<T> findMessageBuilder(Class<T> eventType) {
        return (MessageBuilder<T>) messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(eventType))
                .findFirst()
                .orElseThrow(() -> new MessageBuilderNotFoundException(
                        "No MessageBuilder found for event type: " + eventType.getName()
                ));
    }
}