package faang.school.notificationservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.messageBuilder.MentorshipOfferBuilder;
import faang.school.notificationservice.service.post.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final MentorshipOfferBuilder mentorshipOfferBuilder;
    private final List<NotificationService> postServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipOfferedEventDto mentorshipOfferedEventDto;
        try {
            mentorshipOfferedEventDto = objectMapper.readValue(message.getBody(), MentorshipOfferedEventDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String text = mentorshipOfferBuilder.createMessage(mentorshipOfferedEventDto);

        postServices.stream()
                .filter(postServices -> postServices.isPreferredContact(mentorshipOfferedEventDto))
                .forEach(postService -> postService.send(mentorshipOfferedEventDto, text));
    }
}