package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public class EventListener<T> {
    private final String NOTIFICATION_IS_SENT = "Notification is sent to userId={} username={} via {}";
    private final String ERROR_MESSAGE_NO_BUILDER = "No message builder found for given event type: ";
    public final List<NotificationService> notificationServices;
    public final List<MessageBuilder<T>> messageBuilders;
    public final UserServiceClient userServiceClient;
    public final ObjectMapper objectMapper;

    public String getMessage(T event, Locale locale) {
       return messageBuilders.stream()
                .filter(mb -> mb.getInstance() == event.getClass())
                .findFirst()
                .map(mb -> mb.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NO_BUILDER + event.getClass().getName()));
    }

    public void sendNotification(Long id, String subject, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(ns -> ns.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .ifPresent(ns -> ns.send(userDto.getEmail(), subject,  message));
        log.info(NOTIFICATION_IS_SENT,id, userDto.getUsername(), userDto.getPreference());
    }
}
