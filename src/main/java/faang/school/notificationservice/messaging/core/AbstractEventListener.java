package faang.school.notificationservice.messaging.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<E> {
    private final ObjectMapper mapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<? extends E>> messageBuilders;

    /**
     * Тип события, который слушает конкретный листенер.
     */
    protected abstract Class<E> getEventType();

    /**
     * JSON → Event.
     */
    protected E readEvent(String json) {
        try {
            E event = mapper.readValue(json, getEventType());
            if (log.isDebugEnabled()) {
                log.debug("Deserialized {}: {}",
                        getEventType().getSimpleName(),
                        abbreviate(json, 200));
            }
            return event;
        } catch (Exception e) {
            log.error("Failed to deserialize {} from JSON ({} chars)",
                    getEventType().getSimpleName(),
                    json != null ? json.length() : 0, e);
            throw new IllegalArgumentException("Failed to deserialize event " + getEventType(), e);
        }
    }

    /**
     * Подбор MessageBuilder по классу события и сборка текста.
     */
    public String getMessage(E event, Locale locale) {
        Class<?> type = event.getClass();
        @SuppressWarnings("unchecked")
        MessageBuilder<E> builder = (MessageBuilder<E>) messageBuilders.stream()
                .filter(b -> b.getInstance().equals(type))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("No MessageBuilder found for event type {}", type.getName());
                    return new IllegalStateException("No message builder for event type " + type.getName());
                });

        Locale effectiveLocale = (locale == null ? Locale.getDefault() : locale);

        if (log.isTraceEnabled()) {
            log.trace("Using {} for type {}, locale={}",
                    builder.getClass().getSimpleName(), type.getSimpleName(), effectiveLocale);
        }

        String message = builder.buildMessage(event, effectiveLocale);

        if (log.isDebugEnabled()) {
            log.debug("Built message for {} (len={}): {}",
                    type.getSimpleName(),
                    message != null ? message.length() : 0,
                    abbreviate(message, 200));
        }

        return message;
    }

    /**
     * Загрузка пользователя, выбор сервиса по preference и отправка.
     */
    public void sendNotification(long userId, String message) {
        final UserDto user;
        try {
            user = userServiceClient.getUser(userId);
            if (log.isDebugEnabled()) {
                log.debug("Loaded user {} with preference {}", userId, user.getPreference());
            }
        } catch (FeignException.NotFound nf) {
            log.warn("User {} not found in user-service", userId);
            throw new faang.school.notificationservice.error.UserNotFoundException(userId);
        } catch (FeignException fx) {
            log.error("User-service error {} for userId={}", fx.status(), userId, fx);
            throw new IllegalStateException("User service error: " + fx.status() + " for userId=" + userId, fx);
        }

        NotificationService service = notificationServices.stream()
                .filter(s -> s.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> {
                    log.error("No NotificationService matches preference {} for user {}",
                            user.getPreference(), userId);
                    return new IllegalStateException("No NotificationService for contact " + user.getPreference());
                });

        log.info("Sending notification via {} to user {} (preference={})",
                service.getClass().getSimpleName(), userId, user.getPreference());

        service.send(user, message);
    }

    private static String abbreviate(String s, int max) {
        if (s == null) return "null";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max)) + "...";
    }
}