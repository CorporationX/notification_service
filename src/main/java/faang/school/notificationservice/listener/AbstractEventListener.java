package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale userLocale){
        var messageBuilder = findMessageBuilder(event.getClass());

        return messageBuilder.buildMessage(event, userLocale);
    }

    private MessageBuilder<T> findMessageBuilder(Class<?> eventType){
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportEventType().equals(eventType))
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException("No message builder found for the given type: " + eventType.getName()));
    }

    protected void sendNotification(Long id, String message){
        var user = userServiceClient.getUser(id);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for the user's preferred communications method."))
                .send(user, message);
    }
}
