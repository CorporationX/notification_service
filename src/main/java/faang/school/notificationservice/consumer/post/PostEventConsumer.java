package faang.school.notificationservice.consumer.post;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.PostPublishedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostEventConsumer {

    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<?>> messageBuilders;
    private Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;
    private Map<Class<?>, MessageBuilder<?>> messageBuilderMap;

    @PostConstruct
    public void init() {
        notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
        messageBuilderMap = messageBuilders.stream()
                .collect(Collectors.toMap(MessageBuilder::getEventType, Function.identity()));
    }

    @KafkaListener(topics = "post-published-events",
            groupId = "notification-service",
            properties = "spring.json.value.default.type: faang.school.notificationservice.dto.PostPublishedEvent")
    public void handlePostPublished(PostPublishedEvent event) {
        log.info("Received post published event: {}", event);
        UserDto postAuthor = userServiceClient.getUserById(event.authorId());
        List<UserDto> followers = userServiceClient.getUsersByIds(postAuthor.getFollowersIds());
        @SuppressWarnings("unchecked")
        MessageBuilder<PostPublishedEvent> builder =
                (MessageBuilder<PostPublishedEvent>) messageBuilderMap.get(event.getClass());
        String postPublishedMessage = builder.buildMessage(event, postAuthor, Locale.getDefault());
        for (UserDto follower : followers) {
            notificationServiceMap.get(follower.getPreference()).send(follower, postPublishedMessage);
        }
    }
}
