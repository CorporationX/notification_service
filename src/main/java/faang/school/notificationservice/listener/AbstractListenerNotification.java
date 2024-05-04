package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractListenerNotification<T>{
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notification;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void sendNotification(Long id, String textMessage){
        UserDto user = userServiceClient.getUser(id);
        notification.stream().filter(service -> service.getPreferredContact().equals(user.getContactPreference()))
                .findFirst()
                .ifPresent(service -> service.send(user, textMessage));
    }

    protected String getMessage(T event, Locale locale){
        return messageBuilders.stream().filter(messageBuilder -> messageBuilder.supportsEventType()== event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No found user for notification"));

    }
}
