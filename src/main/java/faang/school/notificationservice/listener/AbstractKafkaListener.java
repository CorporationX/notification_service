package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.comment.CommentNewModel;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractKafkaListener<T> {
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    public String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == CommentNewModel.class)
                .findAny()
                // TODO: другой тип исключения
                .orElseThrow(RuntimeException::new)
                // TODO: локаль из юзера
                .buildMessage(event, locale);
    }

    public void sendNotification(UserClientResponseDto user, String text) {
        notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .findAny()
                .ifPresent(service -> service.send(user, text));
    }
}
