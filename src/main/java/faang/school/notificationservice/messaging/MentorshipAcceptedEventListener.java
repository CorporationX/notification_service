package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.mentorshiprequest.MentorshipAcceptedDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedDto> {

    public MentorshipAcceptedEventListener(JsonMapper jsonMapper, UserServiceClient userServiceClient, List<MessageBuilder<MentorshipAcceptedDto>> messageBuilders, List<NotificationService> notificationServices) {
        super(jsonMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(topics = "mentorship-accepted")
    public void listen(MentorshipAcceptedDto dto) {
        log.info("Received accepted mentorship: {}", dto);

        String message = getMessage(dto, Locale.UK);

        log.info("Sending notification about accepted mentorship to user with id {}: {}", dto.getRequesterId(), message);

        sendNotification(dto.getRequesterId(), message);
    }
}
