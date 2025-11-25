package faang.school.notificationservice.messaging.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserDto.PreferredContact;
import faang.school.notificationservice.error.UserNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEventListener<E> {

    private final ObjectMapper mapper;
    private final UserServiceClient userServiceClient;
    private final Map<Class<?>, MessageBuilder<?>> buildersByType;
    private final Map<PreferredContact, NotificationService> notificationServiceByPreference;

    @Value("${app.core.max_logged_json_length:200}")
    private int maxLoggedJsonLength;

    @Value("${app.locale.default:en}")
    private String defaultLocale;

    protected AbstractEventListener(ObjectMapper mapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<?>> messageBuilders) {

        this.mapper = mapper;
        this.userServiceClient = userServiceClient;

        this.buildersByType = messageBuilders.stream()
                .collect(Collectors.toUnmodifiableMap(
                        MessageBuilder::getInstance,
                        Function.identity()
                ));

        this.notificationServiceByPreference = notificationServices.stream()
                .collect(Collectors.toUnmodifiableMap(
                        NotificationService::getPreferredContact,
                        Function.identity()
                ));
    }

    protected abstract Class<E> getEventType();

    protected E readEvent(String json) {
        try {
            E event = mapper.readValue(json, getEventType());

            log.debug("Deserialized {}: {}",
                    getEventType().getSimpleName(),
                    abbreviate(json, maxLoggedJsonLength));

            return event;

        } catch (Exception e) {
            log.error("Failed to deserialize {} from JSON ({} chars)",
                    getEventType().getSimpleName(),
                    json != null ? json.length() : 0, e);

            throw new IllegalArgumentException(
                    String.format("Failed to deserialize event %s", getEventType().getSimpleName()), e
            );
        }
    }

    protected String getMessage(E event, Locale locale) {
        Class<?> type = event.getClass();

        @SuppressWarnings("unchecked")
        MessageBuilder<E> builder = (MessageBuilder<E>) buildersByType.get(type);

        if (builder == null) {
            log.warn("No MessageBuilder found for event type {}", type.getName());
            throw new IllegalStateException(
                    String.format("No message builder for event type %s", type.getName()));
        }

        Locale effectiveLocale = Optional.ofNullable(locale).orElse(Locale.getDefault());

        if (log.isTraceEnabled()) {
            log.trace("Using {} for {}, locale={}",
                    builder.getClass().getSimpleName(),
                    type.getSimpleName(),
                    effectiveLocale);
        }

        String message = builder.buildMessage(event, effectiveLocale);

        log.debug("Built message for {} (len={}): {}",
                type.getSimpleName(),
                message != null ? message.length() : 0,
                abbreviate(message, maxLoggedJsonLength));

        return message;
    }

    protected UserDto loadUser(long userId) {
        try {
            UserDto user = userServiceClient.getUser(userId);
            log.debug("Loaded user {} with preference {}", userId, user.preference());
            return user;
        } catch (FeignException.NotFound nf) {
            log.warn("User {} not found in user-service", userId);
            throw new UserNotFoundException(userId);
        } catch (FeignException fx) {
            log.error("User-service error {} for userId={}", fx.status(), userId, fx);
            throw new IllegalStateException(
                    String.format("User service error: %s for userId=%s", fx.status(), userId),
                    fx
            );
        }
    }

    protected Locale resolveLocale(String userLocale) {
        if (userLocale == null || userLocale.isBlank()) {
            return Locale.forLanguageTag(defaultLocale);
        }
        return Locale.forLanguageTag(userLocale);
    }

    protected void sendNotification(UserDto user, String message) {
        final PreferredContact preferred;
        try {
            preferred = user.preferredContact();
        } catch (IllegalArgumentException ex) {
            log.error("Unknown preferred contact '{}' for user {}", user.preference(), user.id(), ex);
            throw new IllegalStateException(
                    String.format("Unknown preferred contact '%s' for user %s",
                            user.preference(), user.id()),
                    ex
            );
        }

        NotificationService service = notificationServiceByPreference.get(preferred);
        if (service == null) {
            log.error("No NotificationService matches preference {} for user {}", preferred, user.id());
            throw new IllegalStateException("No NotificationService for contact " + preferred);
        }

        log.info("Sending notification via {} to user {} (preference={})",
                service.getClass().getSimpleName(), user.id(), preferred);

        service.send(user, message);
    }

    private static String abbreviate(String s, int max) {
        if (s == null) return "null";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max)) + "...";
    }
}