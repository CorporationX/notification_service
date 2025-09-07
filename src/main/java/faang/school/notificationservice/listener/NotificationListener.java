package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationServiceResolver;
import faang.school.notificationservice.service.user.FeignUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class NotificationListener<T> {

    private final List<MessageBuilder<T>> builders;
    private final NotificationServiceResolver notificationServiceResolver;
    private final FeignUserService feignUserService;

    protected final void handle(T event) {
        log.info("Received {}: {}", event.getClass().getSimpleName(), event);

        UserDto recipient = feignUserService.getById(recipientId(event));

        String message = resolveMessageBuilder(event).buildMessage(event, Locale.getDefault());
        notificationServiceResolver.getServiceForPreferredContact(recipient.getPreference())
                .send(recipient, message);

        log.info("Notification sent to user {} via {}", recipient.getId(), recipient.getPreference());
    }

    protected abstract Long recipientId(T event);

    private MessageBuilder<T> resolveMessageBuilder(T event) {
        return builders.stream()
                .filter(builder -> builder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new MessageBuilderNotFoundException(
                        "No MessageBuilder found for event type: " + event.getClass().getName()));
    }
}
