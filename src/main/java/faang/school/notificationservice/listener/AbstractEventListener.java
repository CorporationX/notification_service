package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

public abstract class AbstractEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private Class<T> eventDtoClass;
    private List<MessageBuilder<T>> messageBuilders;
    private List<NotificationService> notificationServices;


    public AbstractEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient) {
        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventDtoClass);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String text, UserDto receiver) {
        getNotificationService(receiver).send(receiver, text);
    }

    public MessageBuilder<T> getBuilder() {
        return messageBuilders.stream()
                .findFirst()
                .orElseThrow( () -> new EntityNotFoundException("Message builder not found"));
    }

    public NotificationService getNotificationService(UserDto receiver) {
        return notificationServices.stream()
                .filter(service -> service.getPreferredContact() == receiver.getPreference())
                .findFirst()
                .orElseThrow( () -> new EntityNotFoundException("Notification service not found"));
    }


}
