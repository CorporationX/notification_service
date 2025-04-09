package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected ObjectMapper objectMapper;
    protected UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;
    
    protected String getMessage(T event, Locale locale) {
        MessageBuilder<T> builder = messageBuilders.stream()
                .filter(b -> b.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No MessageBuilder found for: " + event.getClass().getName()));

        return builder.buildMessage(event, locale);
    }

    protected void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        UserDto.PreferredContact preferredContact = user.getPreference();

        NotificationService notificationService = notificationServices.stream()
                .filter(s -> s.getPreferredContact() == preferredContact)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No NotificationService for contact: " + preferredContact));

        notificationService.send(user, message);
    }
}
