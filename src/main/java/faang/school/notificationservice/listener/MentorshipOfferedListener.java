package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private UserServiceClient userServiceClient;
    private List<NotificationService> notificationServices;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipOfferedEventDto mentorshipOfferedEventDto =
                    objectMapper.readValue(message.getBody(), MentorshipOfferedEventDto.class);
            UserDto requester = userServiceClient.getUser(mentorshipOfferedEventDto.getRequesterId());
            UserDto receiver = userServiceClient.getUser(mentorshipOfferedEventDto.getReceiverId());
            String text = "You've got a new request on mentorship from: " + requester.getUsername();
            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == receiver.getPreference())
                    .findFirst()
                    .ifPresent(service -> service.send(receiver, text));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
