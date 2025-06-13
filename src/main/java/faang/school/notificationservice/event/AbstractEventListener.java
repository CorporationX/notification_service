package faang.school.notificationservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.FailedToDeserializeException;
import faang.school.notificationservice.exception.FetchViaFeignException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.NotificationServiceNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T extends Event> implements EventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    private Map<Class<? extends Event>, MessageBuilder<T>> messageBuilderMap;
    private Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;

    @Value("${kafka.trace.id.header}")
    private String traceIdHeader;

    @PostConstruct
    public void init() {
        messageBuilderMap = messageBuilders.stream()
                .collect(Collectors.toMap(MessageBuilder::supportsEventType, Function.identity()));
        notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
    }

    @Override
    public void handleEvent(ConsumerRecord<String, String> message, Class<T> type, Consumer<T> consumer) {
        T event;
        try {
            event = objectMapper.readValue(message.value(), type);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message: {}", message.value(), e);
            throw new FailedToDeserializeException(message.value());
        }
        Header traceIdHeader = message.headers().lastHeader(this.traceIdHeader);
        if (traceIdHeader != null && traceIdHeader.value() != null) {
            event.setTraceId(new String(traceIdHeader.value(), StandardCharsets.UTF_8));
        } else {
            log.warn("No '{}' header found in message from topic={}, partition={}",
                    traceIdHeader, message.topic(), message.partition());
        }
        consumer.accept(event);
    }

    @Override
    public String getMessage(T event, Locale locale) {
        MessageBuilder<T> messageBuilder = messageBuilderMap.get(event.getClass());
        if (messageBuilder == null) {
            log.error("No message builder found for event type: {}", event.getEventType());
            throw new MessageBuilderNotFoundException(event.getEventType());
        }
        return messageBuilder.buildMessage(event, locale);
    }

    @Override
    public void sendNotification(Long userId, String message) {
        UserDto user;
        try {
            user = userServiceClient.getUser(userId);
        } catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", userId, e.getMessage(), e);
            throw new FetchViaFeignException(userId);
        }
        UserDto.PreferredContact preferredContact = user.getPreference();
        NotificationService notificationService = notificationServiceMap.get(preferredContact);
        if (notificationService == null) {
            log.error("No notification service found for preferred contact: {}", preferredContact);
            throw new NotificationServiceNotFoundException(preferredContact);
        }
        notificationService.send(user, message);
    }
}
