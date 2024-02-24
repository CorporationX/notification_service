package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class AbstractEventListener<EventType> {
    private final ObjectMapper objectMapper;
    private final List<NotificationService> services;
    private final List<MessageBuilder<EventType>> messageBuilders;
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    public void buildAndSendMessage(Message message, Class<EventType> type) {
        try {
            EventType event = objectMapper.readValue(message.getBody(), type);
            MessageBuilder<EventType> messageBuilder = findMessageBuilder(type);

            sendTextToUser(event, messageBuilder);
        } catch (IOException e) {
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
        UserDto receiver = getUserDto(receiverId);

        String textMessage = messageBuilder.buildMessage(event, receiver.getLocale());

        services.stream().filter(service -> service.getPreferredContact() == receiver.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Service not found for event type " + event.getClass()))
                .send(receiver, textMessage);
    }

    private UserDto getUserDto(long receiverId) {
        userContext.setUserId(0);
        UserDto receiver = userServiceClient.getUser(receiverId);
        receiver.setLocale(Locale.US);
        receiver.setPreference(UserDto.PreferredContact.PHONE);
        return receiver;
    }
}