package faang.school.notificationservice.listener;

import faang.school.notificationservice.config.client.UserFeignClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public abstract class AbstractNotificationClass<T> {
    private final UserFeignClient userFeignClient;
    private final List<NotificationService> notificationService;
    private final List<MessageBuilder<T>> messageBuilders;

    public void sendNotification(Long usersId, String message) {
        UserDto dto = userFeignClient.getUserDto(usersId);
        if (dto == null) {
            throw new EntityNotFoundException("This user does not exist");
        }
        NotificationService preferredService = notificationService.stream()
                .filter(service ->
                        Objects.equals(service.getPreferredContact(), dto.getPreference()))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("The user has not specified their preferred notification type"));

        preferredService.send(dto, message);
    }

    public String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("There is no suitable builder for this class, %s"
                        .formatted(event.getClass())))
                .buildMessage(event, locale);
    }
}