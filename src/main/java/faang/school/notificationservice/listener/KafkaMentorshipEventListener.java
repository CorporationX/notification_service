package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipRequestDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class KafkaMentorshipEventListener extends AbstractEventListener<MentorshipRequestDto> {
    public KafkaMentorshipEventListener(UserServiceClient userServiceClient,
                                        List<NotificationService> notificationServices,
                                        List<MessageBuilder<MentorshipRequestDto>> messageBuilders) {
        super(userServiceClient, notificationServices, messageBuilders);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.mentorship-accept}",
            groupId = "${spring.kafka.group-id}",
            containerFactory = "mentorshipEventKafkaListenerContainerFactory"
    )
    public void listen(MentorshipRequestDto event, Acknowledgment acknowledgment) {
        try {
            String localizedMessage = getMessage(event, Locale.JAPAN);
            sendNotification(event.getRequesterId(), localizedMessage);
            acknowledgment.acknowledge();
        } catch (Exception exception) {
            log.warn("Failed to notify user {} about accepted mentorship by user {}.",
                    event.getRequesterId(), event.getReceiverId(), exception);
        }
    }
}
