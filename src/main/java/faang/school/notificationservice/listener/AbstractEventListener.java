package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T extends NotificationEvent> {

    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;

    protected abstract boolean isEventValid(T event);

    protected void sendNotification(T event) {
        sendNotification(event, getMessage(event));
    }

    protected void sendNotification(T event, String message) {
        if (isEventValid(event)) {
            sendMessage(event.getOwner(), message);
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

    protected String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }


    protected void sendMessage(UserDto userDto, String message) {
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No notification service was found for user preferred notification type"))
                .send(userDto, message);
    }

    @SafeVarargs
    protected final boolean validateObjectNonNullData(Object o, Supplier<Object>... fieldGetters) {
        return Objects.nonNull(o) || Arrays.stream(fieldGetters).map(Supplier::get).noneMatch(Objects::isNull);
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