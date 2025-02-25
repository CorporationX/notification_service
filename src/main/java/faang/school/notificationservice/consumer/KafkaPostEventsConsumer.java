package faang.school.notificationservice.consumer;

import faang.school.notificationservice.builder.LikePostEventBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class KafkaPostEventsConsumer {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;
    private final LikePostEventBuilder likePostEventBuilder;
    private final List<NotificationService> notificationServices;

    @KafkaListener(topics = "${spring.kafka.like_post_event_topic_name}", groupId = "group-id")
    public void onLikePostEvent(String message) {
        System.out.println("LIKE EVENT DETECTED: " + message);
        LikePostEvent event = likePostEventBuilder.build(message);
        UserDto userDto = userServiceClient.getUser(event.authorId());
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .ifPresent(service -> service.send(userDto,
                        messageSource.getMessage("post.like", null, Locale.getDefault())));
    }
}
