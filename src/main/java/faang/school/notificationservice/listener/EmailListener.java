package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EmailEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServiceList;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            EmailEvent event = objectMapper.readValue(message.getBody(), EmailEvent.class);
            log.info("Accepted JSON in notification service email");
            UserDto user = userServiceClient.getUser(event.getUserId());
            log.info("Send DTO to the notification service email");
            user.setPreference(UserDto.PreferredContact.EMAIL);
            notificationServiceList.stream()
                    .filter(service -> service.getPreferredContact() == user.getPreference())
                    .findFirst()
                    .ifPresent(service -> service.send(user, "You have purchased a premium subscription"));
            log.info("Email sent to user {}", user.getId());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
