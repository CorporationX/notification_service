package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.message_builder.MentorshipOfferedEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipOfferedEventListener {

    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;
    private final MentorshipOfferedEventMessageBuilder mentorshipOfferedEventMessageBuilder;
    private final UserService userService;

    @PostConstruct
    public void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "${kafka.topic.mentorship-offer}")
    public void onMessage(String event) {
        MentorshipOfferedEvent mentorshipOfferedEvent = deserializeEvent(event);

        UserDto mentor = userService.getUser(mentorshipOfferedEvent.mentorId());
        String text = mentorshipOfferedEventMessageBuilder.buildMessage(mentorshipOfferedEvent, mentor.getLocale());


        for (NotificationService notificationService : notificationServices) {
            if (notificationService.getPreferredContact().equals(mentor.getPreference())) {
                notificationService.send(mentor, text);
                break;
            }
        }
    }

    private MentorshipOfferedEvent deserializeEvent(String event) {
        MentorshipOfferedEvent mentorshipOfferedEvent = null;
        try {
            mentorshipOfferedEvent = objectMapper.readValue(event, MentorshipOfferedEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize event {}", event);
            //skip
        }
        return mentorshipOfferedEvent;
    }
}