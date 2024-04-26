package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final MessageBuilder<T> messageBuilder;
    protected final List<NotificationService> notificationServices;
    protected final UserServiceClient userServiceClient;
    protected final ObjectMapper objectMapper;

    public String getMessage(T eventType, Locale locale){
        return messageBuilder.buildMessage(eventType, locale);
    }

    public void sendNotification(long userId, String message){
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresent(notificationService -> notificationService.send(user, message));
    }
}
