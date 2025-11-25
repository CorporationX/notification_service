package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MentorshipOfferedEventListener extends AbstractEventListener {

    public MentorshipOfferedEventListener(
            ObjectMapper objectMapper,
            UserService userService,
            List<NotificationService> notificationServices,
            List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, userService, notificationServices, messageBuilders);
    }

    @KafkaListener(topics = "${kafka.topics.mentorship-offer}")
    public void onMessage(MentorshipOfferedEvent event) {
        log.info("Received mentorship offered event: {}", event);

        UserDto mentor = userService.getUser(event.mentorId());
        String message = getMessage(event, MentorshipOfferedEvent.class, mentor.getLocale());

        sendNotification(event.mentorId(), message);

        log.info("Successfully sent mentorship notification to user {}", event.mentorId());
    }
}