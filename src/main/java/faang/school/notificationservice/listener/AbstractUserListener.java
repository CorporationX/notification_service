package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractUserListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServiceList;
    private final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(T event, Locale userLocale){
        return messageBuilders.stream()
                .filter(messageBuilders -> messageBuilders.supportsEventType() == event.getClass())
                .findFirst()
                .map(messageBuilders -> messageBuilders.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("No message builedr found for the given event type: " + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message){
        UserDto user = userServiceClient.getUser(id);
        notificationServiceList.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The user has not selected a method for sending notifications"))
                .send(user, message);
    }
}
