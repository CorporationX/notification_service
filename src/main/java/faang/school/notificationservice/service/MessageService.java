package faang.school.notificationservice.service;

import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final List<MessageBuilder<?>> messageBuilders;
    private Map<Class<?>, MessageBuilder<?>> builderMap;

    @PostConstruct
    public void init() {
        builderMap = messageBuilders.stream()
                .collect(Collectors.toMap(
                        MessageBuilder::getInstance,
                        Function.identity()
                ));
        log.info("Initialized MessageService with {} builders", builderMap.size());
    }

    public <T> String buildMessage(Class<T> eventType, T event, Locale locale) {
        return getMessageBuilder(eventType).buildMessage(event, locale);
    }

    @SuppressWarnings("unchecked")
    private <T> MessageBuilder<T> getMessageBuilder(Class<T> eventType) {
        MessageBuilder<?> builder = builderMap.get(eventType);
        if (builder == null) {
            log.error("No MessageBuilder found for event type: {}", eventType.getName());
            throw new MessageBuilderNotFoundException(
                    "No MessageBuilder found for event type: " + eventType.getName()
            );
        }
        return (MessageBuilder<T>) builder;
    }
}