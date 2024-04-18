package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener {

    private final MessageBuilder<CommentEvent> messageBuilder;
    private final List<NotificationService> notificationServices;
    private final UserServiceClient userServiceClient;

    @KafkaListener(topics = "${spring.data.kafka.channels.comment-channel.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(CommentEvent commentEvent) {
        sendNotification(commentEvent.getPostAuthorId(), getMessage(commentEvent, Locale.ENGLISH));
    }

    private String getMessage(CommentEvent commentEvent, Locale locale) {
        return messageBuilder.buildMessage(commentEvent, locale);
    }

    private void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Preferred contact not found"))
                .send(userDto, message);
    }
}
