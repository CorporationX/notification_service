package faang.school.notificationservice.listener.subscription;

import faang.school.notificationservice.event.kafka.ViewProfileEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ViewUserProfileListener extends AbstractEventListener<ViewProfileEvent> {
    public ViewUserProfileListener(List<NotificationService> notificationServices,
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
        return validateObjectNonNullData(event, event::getOwner, event::getFollower)
                && isUserDtoValid(event.getOwner())
                && isUserDtoValid(event.getFollower());
    }
}
