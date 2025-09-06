package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.SkillOfferEvent;
import faang.school.notificationservice.exception.NonRetryableException;
import faang.school.notificationservice.exception.RetryableException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class SkillOfferListener extends AbstractEventListener<SkillOfferEvent> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    public SkillOfferListener(List<MessageBuilder<SkillOfferEvent>> messageBuilders,
                              List<NotificationService> notificationServices,
                              ObjectMapper objectMapper,
                              UserServiceClient userServiceClient) {
        super(messageBuilders, notificationServices);
        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.skillOffer}",
            groupId = "skill"
    )
    public void consume(String data) {
        try {
            SkillOfferEvent skillOfferEvent = objectMapper.readValue(data, SkillOfferEvent.class);
            log.info("New event id={} received from Kafka: {}", skillOfferEvent.id(), data);
            String message = getMessage(skillOfferEvent, Locale.getDefault());
            log.debug("For event id={} built message={}", skillOfferEvent.id(), message);
            UserDto receiver = userServiceClient.getUser(skillOfferEvent.receiverId());
            log.debug("Got receiver id={} from user-service", skillOfferEvent.id());

            sendNotification(receiver, message);

            log.info("Notification sent to user id={} with text='{}'", receiver.getId(), message);
        } catch (RetryableException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NonRetryableException(e);
        }
    }
}
