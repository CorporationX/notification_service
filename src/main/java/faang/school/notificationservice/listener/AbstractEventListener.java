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
public class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message not found"))
                .buildMessage(event, locale);
    }

    protected void sendNotification(long receiverId, String message) {
        UserDto user = userServiceClient.getUser(receiverId);
//        notificationServices.stream()
//                .filter(service -> service.getPreferredContact() == user.getPreference())
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Preference not found"));
//                .send(user, message);
        // как будет
        System.out.println("Message sent: " + message);
    }
}
