package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.kafka.SubscriptionEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SubscriptionEventListener extends AbstractEventListener<SubscriptionEvent> {

    public SubscriptionEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices,
            MessageBuilder<SubscriptionEvent> messageBuilder,
            List<MessageBuilder<SubscriptionEvent>> messageBuilders
    ) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilder, messageBuilders);
    }

    @KafkaListener(
            topics = "${spring.data.kafka.topic.subscription}",
            groupId = "${spring.data.kafka.consumer-group.notification}",
            containerFactory = "kafkaSubscriptionEventListener"
    )
    public void handle(SubscriptionEvent event) {
        sendNotification(event);
    }

    @Override
    protected boolean isEventValid(SubscriptionEvent event) {
        boolean eventIsValid = validateObjectNonNullData(event, event::getSubscriptionEventType);
        boolean ownerIsValid = isUserDtoValid(event.getOwner());
        boolean followerIsValid = isUserDtoValid(event.getFollower());
        return eventIsValid && ownerIsValid && followerIsValid;
    }

    private boolean isUserDtoValid(UserDto userDto) {
        return validateObjectNonNullData(
                userDto,
                userDto::getId,
                userDto::getUsername,
                userDto::getPhone,
                userDto::getEmail
        );
    }
}