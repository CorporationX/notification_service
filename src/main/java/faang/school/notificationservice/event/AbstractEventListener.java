package faang.school.notificationservice.event;

import faang.school.notificationservice.client.FeignUserServiceAdapter;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.FetchViaFeignException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.NotificationServiceNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T extends Event> implements EventListener<T> {
    private final List<MessageBuilder<? extends Event>> messageBuilders;
    private final List<NotificationService> notificationServices;
    private final FeignUserServiceAdapter feignUserServiceAdapter;

    private Map<Class<? extends Event>, MessageBuilder<? extends Event>> messageBuilderMap;
    private Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;

    @PostConstruct
    public void init() {
        messageBuilderMap = messageBuilders.stream()
                .collect(Collectors.toMap(MessageBuilder::supportsEventType, Function.identity()));
        notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
    }

    @Override
    public void handleEvent(T event, Consumer<T> consumer) {
        log.info("AbstractEventListener: Received event: {}", event.toString());
        consumer.accept(event);
    }

    @Override
    public String getMessage(T event, Locale locale) {
        log.info("Trying to get message for event {}", event.getClass().getName());
        MessageBuilder<T> messageBuilder = getBuilder(event.getClass());
        if (messageBuilder == null) {
            log.error("No message builder found for event type: {}", event.getEventType());
            throw new MessageBuilderNotFoundException(event.getEventType());
        }
        return messageBuilder.buildMessage(event, locale);
    }

    @Override
    public void sendNotification(UserDto user, String message) {
        log.info("Trying to send message '{}' to user {}", message, user.getUsername());
        UserDto.PreferredContact preferredContact = user.getPreference();
        NotificationService notificationService = notificationServiceMap.get(preferredContact);
        if (notificationService == null) {
            log.error("No notification service found for preferred contact: {}", preferredContact);
            throw new NotificationServiceNotFoundException(preferredContact);
        }
        notificationService.send(user, message);
    }

    protected UserDto getUser(Long userId, String eventNameForLog, Long eventIdForLog) {
        return feignUserServiceAdapter.fetchUserDtosViaFeign(userId, eventNameForLog, eventIdForLog)
                .orElseThrow(() -> new FetchViaFeignException(userId));
    }

    @SuppressWarnings("unchecked")
    private MessageBuilder<T> getBuilder(Class<? extends Event> eventType) {
        return (MessageBuilder<T>) messageBuilderMap.get(eventType);
    }
}
