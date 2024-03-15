package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public abstract class AbstractEventListener<T> implements MessageListener {
    private ObjectMapper objectMapper;
    private Class<T> eventType;
    private List<MessageBuilder<T>> messageBuilders;
    private List<NotificationService> notificationServices;
    private UserServiceClient userServiceClient;

    @Autowired
    public void init(ObjectMapper objectMapper, List<MessageBuilder<T>> messageBuilders, List<NotificationService> notificationServices, UserServiceClient userServiceClient) {
        this.objectMapper = objectMapper;
        this.messageBuilders = messageBuilders;
        this.notificationServices = notificationServices;
        this.userServiceClient = userServiceClient;
    }

    public AbstractEventListener(Class<T> eventType) {
        this.eventType = eventType;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            processEvent(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processEvent(T event) {
        UserDto userDto = userServiceClient.getUser(getUserId(event));
        PreferredContact preference = userDto.getPreference();

        String message = messageBuilders.stream()
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event))
                .orElseThrow(() -> new EntityNotFoundException("MessageBuilder не найден"));

        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(preference))
                .findFirst()
                .ifPresent(service -> service.send(userDto, message));
    }

    protected abstract Long getUserId(T event);
}
