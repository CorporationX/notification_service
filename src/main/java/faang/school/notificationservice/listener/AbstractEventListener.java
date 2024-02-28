package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<EventType> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<NotificationService> services;
    private final MessageBuilder<EventType> messageBuilder;
    private final UserServiceClient userServiceClient;

    public void buildAndSendMessage(Message message, Class<EventType> type) {
        try {
            EventType event = objectMapper.readValue(message.getBody(), type);

            sendTextToUser(event, messageBuilder);
        } catch (IOException e) {
            log.error("IOException trying read value from json to event {}", type);
            throw new RuntimeException(e);
        }
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