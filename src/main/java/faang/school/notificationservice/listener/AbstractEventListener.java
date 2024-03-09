package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

public abstract class AbstractEventListener<T> implements MessageListener {
    private ObjectMapper objectMapper;
    private Class<T> eventDtoClass;
    private List<MessageBuilder<T>> messageBuilders;
    private List<NotificationService> notificationServices;

    protected AbstractEventListener(Class<T> eventDtoClass) {
        this.eventDtoClass = eventDtoClass;
    }

    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    private void setMessageBuilders(List<MessageBuilder<T>> messageBuilders) {
        this.messageBuilders = messageBuilders;
    }

    @Autowired
    private void setNotificationServices(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventDtoClass);
            UserDto receiver = getUser(event);
            sendMessage(buildMessage(event), receiver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String text, UserDto receiver) {
        sendMessage(receiver, text);
    }

    public String buildMessage(T event) {
        return messageBuilders.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Message builder not found"))
                .buildMessage(event);
    }

    public void sendMessage(UserDto receiver, String text) {
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == receiver.getPreference())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Notification service not found"))
                .send(receiver, text);
    }

    public abstract UserDto getUser(T event);
}
