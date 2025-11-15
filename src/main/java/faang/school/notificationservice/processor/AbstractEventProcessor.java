package faang.school.notificationservice.processor;

import faang.school.notificationservice.service.MessageService;
import faang.school.notificationservice.service.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventProcessor<T> implements EventProcessor<T> {

    protected final MessageService messageService;
    protected final NotificationDispatcher notificationDispatcher;

    @Override
    public void process(T event) {
        log.info("Processing event of type {}", getEventType().getSimpleName());

        Locale locale = extractLocale(event);
        long userId = extractUserId(event);

        String message = messageService.buildMessage(getEventType(), event, locale);
        notificationDispatcher.dispatch(userId, message);

        log.info("Event processed successfully");
    }

    protected abstract Locale extractLocale(T event);
    protected abstract long extractUserId(T event);
}