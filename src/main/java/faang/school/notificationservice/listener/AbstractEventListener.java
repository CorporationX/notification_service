package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class AbstractEventListener<EventType> {
    private final ObjectMapper objectMapper;
    private final List<NotificationService> services;
    private final List<MessageBuilder<EventType>> messageBuilders;
    private final UserServiceClient userServiceClient;

    public AbstractEventListener(ObjectMapper objectMapper,
                                 List<NotificationService> services,
                                 List<MessageBuilder<EventType>> messageBuilders,
                                 UserServiceClient userServiceClient) {
        this.objectMapper = objectMapper;
        this.services = services;
        this.messageBuilders = messageBuilders;
        this.userServiceClient = userServiceClient;
    }

    public void buildAndSendMessage(Message message, Class<EventType> type) {
        try {
            EventType event = objectMapper.readValue(message.getBody(), type);
            MessageBuilder<EventType> messageBuilder = findMessageBuilder(type);

            sendTextToUser(event, messageBuilder);
        } catch (IOException e) {
            log.error("IOException trying read value from json to event {}", type);
            throw new RuntimeException(e);
        }
    }

    private MessageBuilder<EventType> findMessageBuilder(Class<EventType> type) {
        return messageBuilders.stream().filter(messageBuilder -> messageBuilder.eventType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message builder not found for event type " + type));
    }

    private void sendTextToUser(EventType event, MessageBuilder<EventType> messageBuilder) {
        long receiverId = messageBuilder.getReceiverId(event);
        UserDto receiver = userServiceClient.getUserUtility(receiverId);
        receiver.setPreference(UserDto.PreferredContact.PHONE);

        String textMessage = messageBuilder.buildMessage(event, receiver.getLocale());

        log.debug("Message built successfully {}", textMessage);
        services.stream().filter(service -> service.getPreferredContact() == receiver.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Service not found for event type " + event.getClass()))
                .send(receiver, textMessage);
    }
}