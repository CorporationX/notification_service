package faang.school.notificationservice.listener.subscription;

import faang.school.notificationservice.event.kafka.UnfollowEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UnfollowEventListener extends AbstractEventListener<UnfollowEvent> {

    public UnfollowEventListener(
            List<NotificationService> notificationServices,
            MessageBuilder<UnfollowEvent> messageBuilder
    ) {
        super(notificationServices, messageBuilder);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.subscription.unfollow-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaUnfollowEventListener"
    )
    public void handle(UnfollowEvent event) {
        sendNotification(event);
    }

    @Override
    protected boolean isEventValid(UnfollowEvent event) {
        boolean eventIsValid = Objects.nonNull(event);
        boolean ownerIsValid = isUserDtoValid(event.getOwner());
        boolean followerIsValid = isUserDtoValid(event.getFollower());
        return eventIsValid && ownerIsValid && followerIsValid;
    }
}