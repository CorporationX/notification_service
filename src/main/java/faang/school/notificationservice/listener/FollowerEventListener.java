package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.util.List;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            UserDto userDto = userServiceClient.getUser(event.followerId());
            List<NotificationService> services = notificationServices.stream()
                    .filter(notificationService -> Objects.equals(notificationService.getPreferredContact(), userDto.getPreference()))
                    .toList();
            NotificationService notificationService = services.get(0);
            if (notificationService == null) {
                throw new RuntimeException("Preferred notification method was not found");
            }
            // тут потом сделать мессадж билдер
            String messageText = "you have been subscribed to by a user with id = %d".formatted(userDto.getId());
            notificationService.send(userDto, messageText);
        } catch (IOException e) {
            log.error("Error parsing from json: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
