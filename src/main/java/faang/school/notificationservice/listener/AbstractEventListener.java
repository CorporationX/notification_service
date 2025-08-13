package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.ErrorType;
import faang.school.notificationservice.exception.NotificationServiceNotFoundException;
import faang.school.notificationservice.exception.ProcessorNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new ProcessorNotFoundException(ErrorType.PROCESSOR_NOT_FOUND))
                .buildMessage(event, locale);
    }

    protected void sendNotification(UserDto user, String message) {
        log.debug("Search for required notification service for {}", user.getPreference().name());
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(
                        () -> new NotificationServiceNotFoundException(ErrorType.NOTIFICATOR_NOT_FOUND))
                .send(user, message)
        ;
    }
}
