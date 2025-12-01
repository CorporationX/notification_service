package faang.school.notificationservice.consumer.post;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.PostPublishedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostEventConsumer {

    private final UserServiceClient userServiceClient;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;
    private final Map<Class<?>, MessageBuilder<?>> messageBuilderMap;

    @Autowired
    public PostEventConsumer(
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices,
            List<MessageBuilder<?>> messageBuilders) {
        this.userServiceClient = userServiceClient;
        this.notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
        this.messageBuilderMap = messageBuilders.stream()
                .collect(Collectors.toMap(MessageBuilder::getEventType, Function.identity()));
    }

    @KafkaListener(topics = "${app.kafka.topics.post-published.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void handlePostPublished(PostPublishedEvent event) {
        log.info("Received post published event: {}", event);
        UserDto postAuthor = userServiceClient.getUserById(event.authorId());
        List<UserDto> followers = userServiceClient.getUsersByIds(postAuthor.getFollowersIds());
        @SuppressWarnings("unchecked")
        MessageBuilder<PostPublishedEvent> builder =
                (MessageBuilder<PostPublishedEvent>) messageBuilderMap.get(event.getClass());
        //TODO: исправить Locale.getDefault(), когда пользователю добавят Locale
        String postPublishedMessage = builder.buildMessage(event, postAuthor, Locale.getDefault());
        for (UserDto follower : followers) {
            notificationServiceMap.get(follower.getPreference()).send(follower, postPublishedMessage);
        }
    }
}
