package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
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

    @Autowired
    public void setMessageBuilders(List<MessageBuilder<T>> messageBuilders) {
        this.messageBuilders = messageBuilders;
    }

    @Autowired
    public void setNotificationServices(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
        UserDto userDto = getUserDto(event);
        UserDto.PreferredContact preference = userDto.getPreference();

        String message = messageBuilders.stream()
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userDto.getUsername()))
                .orElseThrow(() -> new DataValidationException("MessageBuilder не найден"));

        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(preference))
                .findFirst()
                .ifPresent(service -> service.send(userDto, message));
    }

    protected abstract UserDto getUserDto(T event);
}
