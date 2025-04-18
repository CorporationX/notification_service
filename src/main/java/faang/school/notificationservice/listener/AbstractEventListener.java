package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExceptionMessage;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.PreferenceNotFountException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.NotificationType;
import faang.school.notificationservice.repository.NotificationEventLogRepository;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final NotificationEventLogRepository notificationEventLogRepository;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new MessageBuilderNotFoundException(
                        ExceptionMessage.MESSAGE_BUILDER_NOT_FOUND, event.getClass().getSimpleName()))
                .buildMessage(event, locale);
    }

    protected void sendNotification(long receiverId, String message) {
        UserDto user = userServiceClient.getUser(receiverId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new PreferenceNotFountException(ExceptionMessage.PREFERENCE_NOT_FOUND, user.getId()))
                .send(user, message);
    }

    protected boolean checkNotificationExisting(long id, NotificationType notificationType, Acknowledgment ack) {
        if (notificationEventLogRepository.checkExistingEvent(id, notificationType)) {
            ack.acknowledge();
            return true;
        } else {
            notificationEventLogRepository.saveNotificationEvent(id, notificationType);
            return false;
        }
    }

}
