package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T extends NotificationEvent> {

    protected final NotificationSenderService notificationSender;
    private final MessageBuilder<T> messageBuilder;

    public abstract boolean isEventValid(T event);

    protected void sendNotification(T event) {
        if (isEventValid(event)) {
            notificationSender.send(event.getOwner(), getMessage(event));
        } else {
            log.error("Event validation failed. Event: {}", event);
        }
    }

    protected String getMessage(T event) {
        Locale locale = Objects.isNull(event.getOwner().getLocale())
                ? LocaleContextHolder.getLocale()
                : event.getOwner().getLocale();
        return messageBuilder.buildMessage(event, locale);
    }

    @SafeVarargs
    protected final boolean validateObjectNonNullData(Object o, Supplier<Object>... fieldGetters) {
        return Objects.nonNull(o) && Arrays.stream(fieldGetters).map(Supplier::get).noneMatch(Objects::isNull);
    }

    protected boolean isUserDtoValid(UserDto userDto) {
        return validateObjectNonNullData(
                userDto,
                userDto::getId,
                userDto::getUsername,
                userDto::getPhone,
                userDto::getEmail
        );
    }
}