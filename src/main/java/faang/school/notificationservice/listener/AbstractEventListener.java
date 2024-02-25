package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.service.NotificationService;
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
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    @Autowired
    private void setMessageBuilders(List<MessageBuilder<T>> messageBuilders) {
        this.messageBuilders = messageBuilders;
    }

    @Autowired
    private void setNotificationServices(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }


    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventDtoClass);
            UserDto reciever = getUser(event);
            sendMessage(getBuilder().buildMessage(event), reciever);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String text, UserDto receiver) {
        getNotificationService(receiver).send(receiver, text);
    }

    public MessageBuilder<T> getBuilder() {
        return messageBuilders.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Message builder not found"));
    }

    public NotificationService getNotificationService(UserDto receiver) {
        return notificationServices.stream()
                .filter(service -> service.getPreferredContact() == receiver.getPreference())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Notification service not found"));
    }

    public abstract UserDto getUser(T event);
}
