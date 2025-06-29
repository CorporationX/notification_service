package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractKafkaEventListener<T> {
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final UserServiceClient userServiceClient;

    protected String getMessage(Locale locale, T event) {
        MessageBuilder<T> messageBuilder = messageBuilders.stream()
                .filter(ms -> ms.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No required message builder found"));

        return messageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        sendNotification(user, message);
    }

    protected void sendNotification(UserDto user, String message) {
        NotificationService notificationService = notificationServices.stream()
                .filter(ns -> ns.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No required preference found."));
        notificationService.send(user, message);
    }


}
