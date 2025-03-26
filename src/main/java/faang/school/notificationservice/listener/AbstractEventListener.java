package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
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

    public void sendNotification(Long id, String message) {
        UserNotificationDto userNotificationDto = userServiceClient.getNotificationUser(id);
        notificationServices.stream()
                .filter(ns -> ns.getPreferredContact() == userNotificationDto.getPreferredContact())
                .findFirst()
                .ifPresent(ns -> ns.send(userNotificationDto, message));
        log.info(NOTIFICATION_IS_SENT,id, userNotificationDto.getUsername(), userNotificationDto.preferredContact);
    }
}
