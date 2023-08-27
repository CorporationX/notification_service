package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.mentorshiprequest.MentorshipAcceptedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipAcceptedEventListener {

    @KafkaListener(topics = "mentorship-accepted")
    public void listen(MentorshipAcceptedDto dto) {
        log.info("Received accepted mentorship: {}", dto);
    }
}
