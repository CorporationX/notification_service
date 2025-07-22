package faang.school.notificationservice.event;

import faang.school.notificationservice.client.FeignUserServiceAdapter;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RequestEventListener extends AbstractEventListener<RequestEvent> {
    public RequestEventListener(
            MessageBuilder<RequestEvent> messageBuilder,
            List<NotificationService> notificationServices,
            FeignUserServiceAdapter feignUserServiceAdapter) {
        super(messageBuilder, notificationServices, feignUserServiceAdapter);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.request.name}",
            concurrency = "${spring.kafka.topics.request.concurrency}"
    )
    public void onRequestEvent(RequestEvent event) {
        handleEvent(event, requestEvent -> {
            UserDto user = getUser(
                    requestEvent.getUserId(),
                    requestEvent.getEventType(),
                    requestEvent.getIdempotencyKey().getMostSignificantBits()
            );

            String message = getMessage(requestEvent, user.getLocale());
            sendNotification(user, message);
        });
    }
}
