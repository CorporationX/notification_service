package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEventListener {

    protected final ObjectMapper objectMapper;
    protected final UserService userService;
    public final Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;
    public final Map<Class<?>, MessageBuilder<?>> messageBuilderMap;

    protected AbstractEventListener(
            ObjectMapper objectMapper,
            UserService userService,
            List<NotificationService> notificationServices,
            List<MessageBuilder<?>> messageBuilders) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(
                        NotificationService::getPreferredContact,
                        Function.identity()
                ));
        this.messageBuilderMap = messageBuilders.stream()
                .collect(Collectors.toMap(
                        MessageBuilder::getInstance,
                        Function.identity()
                ));
    }

    @PostConstruct
    public void init() {
        log.info("Initializing {} with {} notification services and {} message builders",
                this.getClass().getSimpleName(),
                notificationServiceMap.size(),
                messageBuilderMap.size());
    }

    protected <T> String getMessage(T event, Class<?> eventClass, Locale locale) {
        MessageBuilder<T> messageBuilder = getMessageBuilder(eventClass);
        return messageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userService.getUser(userId);

        NotificationService notificationService = notificationServiceMap.get(user.getPreference());

        if (notificationService == null) {
            log.warn("No notification service found for preference: {}. User ID: {}",
                    user.getPreference(), userId);
            throw new IllegalArgumentException(
                    String.format("No notification service available for preference: %s", user.getPreference())
            );
        }

        log.debug("Sending notification to user {} via {}", userId, user.getPreference());
        notificationService.send(user, message);
    }

    @SuppressWarnings("unchecked")
    private <T> MessageBuilder<T> getMessageBuilder(Class<?> eventClass) {
        MessageBuilder<?> builder = messageBuilderMap.get(eventClass);

        if (builder == null) {
            log.error("No message builder found for event class: {}", eventClass.getName());
            throw new IllegalArgumentException(
                    String.format("No message builder available for event type: %s", eventClass.getName())
            );
        }

        return (MessageBuilder<T>) builder;
    }
}