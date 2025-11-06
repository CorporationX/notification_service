package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.message_builder.MentorshipOfferedEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedEventListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MentorshipOfferedEventMessageBuilder mentorshipOfferedEventMessageBuilder;

    @KafkaListener(topics = "${kafka.topic.mentorship-offer}")
    public void onMessage(String event) {
        MentorshipOfferedEvent mentorshipOfferedEvent = null;
        try {
            mentorshipOfferedEvent = objectMapper.readValue(event, MentorshipOfferedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String text = mentorshipOfferedEventMessageBuilder.buildMessage(mentorshipOfferedEvent, Locale.getDefault());
        UserDto mentor = userServiceClient.getUser(mentorshipOfferedEvent.mentorId());

        for (NotificationService notificationService : notificationServices) {
            if (notificationService.getPreferredContact().equals(mentor.getPreference())) {
                notificationService.send(mentor, text);
            }
        }
    }
}