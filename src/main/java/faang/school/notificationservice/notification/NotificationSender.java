package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public abstract class NotificationSender<T> {
    private final MessageBuilder<T> messageBuilder;
    private final List<NotificationService> notificationServices;

    public void send(T event, UserDto receiver) {
        String message = messageBuilder.buildMessage(event, Locale.UK);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(receiver.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification service not found"))
                .send(receiver, message);
    }
}
