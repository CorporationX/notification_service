package faang.school.notificationservice.listener.subscription;

import faang.school.notificationservice.event.kafka.ViewProfileEvent;
import faang.school.notificationservice.listener.DirectNotificationEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ViewUserProfileListener extends DirectNotificationEventListener<ViewProfileEvent> {
    public ViewUserProfileListener(NotificationSenderService notificationServices,
                                   MessageBuilder<ViewProfileEvent> messageBuilder) {
        super(notificationServices, messageBuilder);
    }
    @KafkaListener(
            topics = "${spring.kafka.topics.profile.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaViewProfileEventListener"
    )
    public void handle(ViewProfileEvent event) {
        sendNotification(event);
    }

    @Override
    public boolean isEventValid(ViewProfileEvent event) {
        return validateObjectNonNullData(event, event::owner, event::getFollower)
                && isUserDtoValid(event.owner())
                && isUserDtoValid(event.getFollower());
    }
}
