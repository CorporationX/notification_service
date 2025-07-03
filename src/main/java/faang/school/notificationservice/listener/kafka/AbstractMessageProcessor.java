package faang.school.notificationservice.listener.kafka;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class AbstractMessageProcessor<T> {
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    public String getMessage(Locale locale, T event) {
        MessageBuilder<T> messageBuilder = messageBuilders.stream()
                .filter(ms -> ms.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No required message builder found"));

        return messageBuilder.buildMessage(event, locale);
    }

    public void sendNotification(UserDto user, String message) {
        NotificationService notificationService = notificationServices.stream()
                .filter(ns -> ns.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No required preference found."));
        notificationService.send(user, message);
    }
}
