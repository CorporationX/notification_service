package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.MentorshipRequestEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MentorshipRequestKafkaListener extends AbstractEventListener<MentorshipRequestEvent> {

    public MentorshipRequestKafkaListener(ObjectMapper objectMapper,
                                          List<MessageBuilder<MentorshipRequestEvent>> messageBuilders,
                                          UserServiceClient userServiceClient,
                                          List<NotificationService> notificationServices) {
        super(objectMapper, messageBuilders, userServiceClient, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.mentorship-request}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(MentorshipRequestEvent event) {
        try {
            UserDto user = userServiceClient.getUser(event.getFoloweeId());
            String text = getMessage(event, user.getLocale());
            sendNotification(user.getId(), text);
            log.info("Mentorship request notification sent successfully to user {}", user.getId());
        } catch (Exception e) {
            log.error("Error processing mentorship request event: {}", event, e);
            throw e;
        }
    }
}