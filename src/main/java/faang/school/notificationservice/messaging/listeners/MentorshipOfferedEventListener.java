package faang.school.notificationservice.messaging.listeners;

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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipOfferedEventListener {

    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;
    private final UserService userService;
    private final MentorshipOfferedEventMessageBuilder mentorshipOfferedEventMessageBuilder;

    private Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;

    @PostConstruct
    public void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "${kafka.topic.mentorship-offer}")
    public void onMessage(MentorshipOfferedEvent mentorshipOfferedEvent) {
        notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(
                        NotificationService::getPreferredContact,
                        Function.identity()
                ));

        UserDto mentor = userService.getUser(mentorshipOfferedEvent.mentorId());
        String text = mentorshipOfferedEventMessageBuilder.buildMessage(mentorshipOfferedEvent, mentor.getLocale());

        NotificationService service = notificationServiceMap.get(mentor.getPreference());

        if (service != null) {
            service.send(mentor, text);
        } else {
            log.warn("No notification service found for preference: {}", mentor.getPreference());
        }
    }
}