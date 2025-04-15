package faang.school.notificationservice.event_listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.BuildMessageFailedException;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AbstractEventListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<?>> messageBuilders;

    @PostConstruct
    private void postConstruct() {
        messageBuilders.stream()
                .collect(Collectors.groupingBy(MessageBuilder::getInstance, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(kvp -> kvp.getValue() > 1)
                .findFirst()
                .ifPresent(kvp -> {
                    throw new IllegalStateException("Each message type must be build by only single builder");
                });
    }

    public String getMessage(Class<?> eventType, Locale locale, Object... args) {
        var messageBuilder = messageBuilders.stream()
                .filter(builder -> builder.getInstance() == eventType)
                .findFirst()
                .orElseThrow(() -> new BuildMessageFailedException(
                        "Builder for message %s is not registered".formatted(eventType.getName())));

        return processMessage(messageBuilder, eventType, locale, args);
    }

    @Async
    public CompletableFuture<Void> sendNotification(long userId, String message) {
        if (message.isBlank()) {
            throw new DataValidationException("Message cannot be blank or empty");
        }

        var userDto = getUser(userId);
        var requiredPreference = userDto.getPreference();
        var sendFutures = notificationServices.stream()
                .filter(service -> service.getPreferredContact() == requiredPreference)
                .map(service -> CompletableFuture.runAsync(() -> service.send(userDto, message)))
                .toList();

        return CompletableFuture.allOf(sendFutures.toArray(new CompletableFuture[0]));
    }

    private UserDto getUser(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (Exception ex) {
            throw new UserNotFoundException("User with id #%d is not found".formatted(userId), ex);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> String processMessage(MessageBuilder<T> builder, Class<?> eventType, Locale locale, Object... args) {
        T event;
        if (args.length > 0 && args[0] instanceof String stringArgument && stringArgument.trim().startsWith("{")) {
            try {
                event = (T) objectMapper.readValue(stringArgument, eventType);
            } catch (Exception e) {
                throw new BuildMessageFailedException(
                        "The first argument cannot be deserialized to type %s".formatted(eventType.getName()),
                        e);
            }
        } else {
            event = createEvent(eventType, args);
        }

        return builder.buildMessage(event, locale);
    }

    @SuppressWarnings("unchecked")
    private <T> T createEvent(Class<?> eventType, Object... args) {
        try {
            if (args.length > 0 && eventType.isInstance(args[0])) {
                return (T) args[0];
            }

            var constructors = eventType.getConstructors();
            for (var constructor : constructors) {
                if (constructor.getParameterCount() == args.length) {
                    return (T) constructor.newInstance(args);
                }
            }
        } catch (Exception e) {
            throw new BuildMessageFailedException(
                    "Failed to create event instance of type %s".formatted(eventType.getName()),
                    e);
        }

        throw new BuildMessageFailedException(
                "Failed to create event instance of type %s: there is not constructor with %d parameters".formatted(
                        eventType.getName(),
                        args.length));
    }
}
