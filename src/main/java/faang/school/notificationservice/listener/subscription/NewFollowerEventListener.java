package faang.school.notificationservice.listener.subscription;

import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.listener.DirectNotificationEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewFollowerEventListener extends DirectNotificationEventListener<NewFollowerEvent> {

    public NewFollowerEventListener(
            NotificationSenderService notificationServices,
            MessageBuilder<NewFollowerEvent> messageBuilder
    ) {
        super(notificationServices, messageBuilder);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.subscription.new-follower-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaNewFollowerEventListener"
    )
    public void handle(NewFollowerEvent event) {
        sendNotification(event);
    }

    @Override
    public boolean isEventValid(NewFollowerEvent event) {
        return validateObjectNonNullData(event, event::owner, event::getFollower)
                && isUserDtoValid(event.owner())
                && isUserDtoValid(event.getFollower());
    }
}