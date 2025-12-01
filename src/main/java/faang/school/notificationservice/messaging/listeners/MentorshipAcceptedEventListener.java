package faang.school.notificationservice.messaging.listeners;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.message_builder.MentorshipAcceptedEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MentorshipAcceptedEventListener {

    private final UserService userService;
    private final MentorshipAcceptedEventMessageBuilder mentorshipAcceptedEventMessageBuilder;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;

    public MentorshipAcceptedEventListener(
            List<NotificationService> notificationServices,
            UserService userService,
            MentorshipAcceptedEventMessageBuilder mentorshipAcceptedEventMessageBuilder) {

        this.notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(
                        NotificationService::getPreferredContact,
                        Function.identity()
                ));
        this.userService = userService;
        this.mentorshipAcceptedEventMessageBuilder = mentorshipAcceptedEventMessageBuilder;
    }

    @KafkaListener(topics = "${kafka.topic.mentorship-accept}")
    public void onMessage(MentorshipAcceptedEvent mentorshipAcceptedEvent) {

        UserDto mentor = userService.getUser(mentorshipAcceptedEvent.mentorId());
        String text = mentorshipAcceptedEventMessageBuilder.buildMessage(mentorshipAcceptedEvent, mentor.getLocale());

        NotificationService service = notificationServiceMap.get(mentor.getPreference());

        if (service != null) {
            service.send(mentor, text);
        } else {
            log.warn("No notification service found for preference: {}", mentor.getPreference());
        }
    }
}