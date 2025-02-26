package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserEventDto;
import faang.school.notificationservice.exception.SmsSendingException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServiceList;
    protected final UserServiceClient userServiceClient;


    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() -> new SmsSendingException("No message builder found for the given event type: "
                        + event.getClass().getName()));
    }

    protected void sendNotification(Long userId, String message) {
        UserEventDto user = userServiceClient.getUserForEvent(userId);
        notificationServiceList.stream()
                .filter(notificationService -> user.getPreference()
                        .equals(notificationService.getPreferredContact(user)))
                .findFirst()
                .orElseThrow(
                        () -> new SmsSendingException("The preferred communication method has not been established."))
                .send(user, message);

    }
}
