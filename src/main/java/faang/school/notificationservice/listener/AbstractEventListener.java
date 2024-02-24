package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
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
public class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final List<NotificationService> services;
    private final List<MessageBuilder<T>> messageBuilders;
    private final UserServiceClient userServiceClient;

    public void buildAndSendMessage(Message message, Class<T> type) {
        try {
            T t = objectMapper.readValue(message.getBody(), type);
            sendTextToUser(t, findMessageBuilder(type));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private MessageBuilder<T> findMessageBuilder (Class<T> type) {
        return messageBuilders.stream().filter(messageBuilder -> messageBuilder.eventType() == type)
                .findFirst()
                .orElseThrow();
    }
    private void sendTextToUser (T event, MessageBuilder<T> messageBuilder) {
        String textMessage = messageBuilder.buildMessage(event, Locale.US);
        long receiverId = messageBuilder.getReceiverId(event);
        UserDto receiver = userServiceClient.getUser(receiverId);

        services.stream().filter(service -> service.getPreferredContact() == receiver.getPreference())
                .findFirst()
                .orElseThrow()
                .send(receiver, textMessage);
    }
}