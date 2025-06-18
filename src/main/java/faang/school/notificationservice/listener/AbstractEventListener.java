package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.EventType;
import faang.school.notificationservice.model.NotificationEvent;
import faang.school.notificationservice.repository.NotificationEventRepository;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final HashMap<UserDto.PreferredContact, NotificationService> preferredContactNotificationServicesMap =
            new HashMap<>();
    protected final HashMap<EventType, MessageBuilder<T>> eventTypeToMessageBuilderMap = new HashMap<>();
    protected final UserServiceClient userServiceClient;
    protected final ObjectMapper objectMapper;
    protected final NotificationEventRepository notificationEventRepository;

    public AbstractEventListener(List<MessageBuilder<T>> messageBuilders,
                                 List<NotificationService> notificationServices,
                                 UserServiceClient userServiceClient,
                                 ObjectMapper objectMapper,
                                 NotificationEventRepository notificationEventRepository) {
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
        this.notificationEventRepository = notificationEventRepository;

        notificationServices.forEach(notificationService ->
                preferredContactNotificationServicesMap.putIfAbsent(
                        notificationService.getPreferredContact(),
                        notificationService)
        );
        messageBuilders.forEach(messageBuilder -> eventTypeToMessageBuilderMap.putIfAbsent(
                messageBuilder.getEventType(),
                messageBuilder)
        );
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.debug("{} received a message:\n{}", getClass().getSimpleName(), message.getBody());
        try {
            T event = convertMessageToEvent(message);
            log.debug("Message was successfully converted to {}:\n{}",
                    event.getClass().getSimpleName(), event);

            NotificationEvent notificationEvent = mapToNotificationEvent(event);
            notificationEventRepository.save(notificationEvent);

            Locale locale = Locale.ENGLISH;// TODO: Заглушка не забыть поменять! Как?
            MessageBuilder<T> messageBuilder = eventTypeToMessageBuilderMap.get(notificationEvent.getEventType());
            sendNotification(notificationEvent.getReceiverId(), messageBuilder.buildMessage(event, locale));
        } catch (IOException e) {
            log.warn("Failed to process message. Message body:\n{}", message.getBody());
            throw new RuntimeException(e);
        }
    }

    public abstract Set<ChannelTopic> getChannelTopics();

    protected abstract Class<T> getEventClass();

    protected abstract NotificationEvent mapToNotificationEvent(T event);

    private T convertMessageToEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), getEventClass());
    }

    private void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        NotificationService notificationService = preferredContactNotificationServicesMap.get(userDto.getPreference());
        notificationService.send(userDto, message);
    }
}
