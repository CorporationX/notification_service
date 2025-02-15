package faang.school.notificationservice.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.entity.Event;
import faang.school.notificationservice.exception.impl.non_retryable.DuplicateEventException;
import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import faang.school.notificationservice.exception.impl.retryable.UserServiceClientException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.EventService;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private static final int REQUIRED_LIST_SIZE = 1;
    private UUID kafkaEventKey;

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;
    protected final EventService eventService;

    protected T handleUniqueEvent(ConsumerRecord<String, Object> kafkaEvent, Class<T> clazz) {
        kafkaEventKey = UUID.fromString(kafkaEvent.key());
        checkEventDuplicatedThrow(kafkaEvent);

        return objectMapper.convertValue(kafkaEvent.value(), clazz);
    }

    protected UserServiceDto getSingleUser(Long userId) {
        return fetchUserWithHandling(() -> userServiceClient.getUser(userId));
    }

    protected List<UserServiceDto> getUserList(List<Long> userIds) {
        return fetchUserWithHandling(() -> userServiceClient.getUsers(userIds));
    }

    protected List<UserServiceDto> getOrderedUsers(List<Long> userIds) {
        return fetchUserWithHandling(() -> userServiceClient.getOrderedUsers(userIds));
    }

    protected String getMessage(T inputDto, UserServiceDto profileOwner, List<String> additionalWordsForOwnerMessage) {
        List<MessageBuilder<T>> builders = messageBuilders.stream()
                .filter(messageBuilder ->
                        Objects.equals(messageBuilder.getInstance(), inputDto.getClass()))
                .toList();

        requireSingleElement(builders, MessageBuilder.class, inputDto.getClass());

        return builders.get(0)
                .buildMessage(inputDto, profileOwner, additionalWordsForOwnerMessage);
    }

    protected void sendSingleNotification(UserServiceDto profileOwner, String message) {
        getNotificationService(profileOwner).send(profileOwner, message);
    }

    protected void sendListOfNotification(List<UserServiceDto> profileOwners, String message) {
        profileOwners.forEach(profileOwner -> sendSingleNotification(profileOwner, message));
    }

    private NotificationService getNotificationService(UserServiceDto user) {
        List<NotificationService> services = notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .toList();
        requireSingleElement(services, NotificationService.class, UserServiceDto.class);

        eventService.save(new Event(kafkaEventKey));
        return services.get(0);
    }

    private void checkEventDuplicatedThrow(ConsumerRecord<String, Object> kafkaEvent) {
        UUID eventId = UUID.fromString(kafkaEvent.key());
        if (eventService.existsById(eventId)) {
            String error = "Duplicate event received: " + eventId;
            log.warn(error);
            throw new DuplicateEventException(error);
        }
    }

    private <R> R fetchUserWithHandling(Supplier<R> fetcher) {
        try {
            return fetcher.get();
        } catch (RuntimeException e) {
            String error = "Fail to get user";
            log.error(error, e);
            throw new UserServiceClientException(error);
        }
    }

    private void requireSingleElement(List<?> list, Class<?> targetClass, Class<?> eventType) {
        int size = list.size();

        if (size == 0) {
            String error = String.format("No %s  found for %s",
                    targetClass.getSimpleName(), eventType.getSimpleName());
            log.error(error);
            throw new NotFoundElementException(error);
        }

        if (list.size() != REQUIRED_LIST_SIZE) {
            String error = String.format("Expected exactly 1 %s for: %s, but got %d",
                    targetClass.getSimpleName(), eventType.getSimpleName(), list.size());
            log.error(error);
            throw new ListSizeNotOneException(error);
        }
    }
}
