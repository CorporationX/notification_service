package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {

    public static final String FAILED_TO_FETCH_USER_WITH_ID = "Failed to fetch user with id {}: {}";
    public static final String NO_MESSAGE_BUILDER_FOUND = "No MessageBuilder found for: ";
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final Map<Class<?>, MessageBuilder<?>> messageBuilderMap;
    protected final List<NotificationService> notificationServices;

    protected String getMessage(@NonNull T event, Locale locale) {
        MessageBuilder<T> builder = (MessageBuilder<T>) messageBuilderMap.get(event.getClass());
        if (builder == null) {
            log.error(NO_MESSAGE_BUILDER_FOUND, event.getClass().getName());
            throw new IllegalArgumentException(NO_MESSAGE_BUILDER_FOUND + event.getClass().getName());
        }
        String message = builder.buildMessage(event, locale);
        log.debug("Successfully built message: {}", message);
        return message;
    }

    protected void sendNotification(@NonNull Long userId, String message) {
        log.info("Sending notification to user with id {}...", userId);

        UserDto user;
        try {
            user = userServiceClient.getUser(userId);
        } catch (FeignException e) {
            log.error(FAILED_TO_FETCH_USER_WITH_ID, userId, e.getMessage(), e);
            return;
        }

        if (user == null) {
            log.error(FAILED_TO_FETCH_USER_WITH_ID, userId, message);
            return;
        }

        UserDto.PreferredContact preferredContact = user.getPreference();
        if (preferredContact == null) {
            user.setPreference(UserDto.PreferredContact.EMAIL);
            log.warn("User with id {} has no preferred contact method. Defaulting to EMAIL.", userId);
        }

        NotificationService notificationService = notificationServices.stream()
                .filter(s -> s.getPreferredContact() == preferredContact)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No NotificationService for contact: " + preferredContact));

        notificationService.send(user, message);
        log.info("Notification successfully sent to user {} via {}", userId, preferredContact);
    }
}
