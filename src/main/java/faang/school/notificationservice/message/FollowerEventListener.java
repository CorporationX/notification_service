package faang.school.notificationservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            FollowerEventDto event = objectMapper.readValue(message.getBody(), FollowerEventDto.class);

            ResourceBundle bundle = ResourceBundle.getBundle("messages");
            String text = bundle.getString("follower.new");

            UserDto user = userServiceClient.getUser(event.followeeId());
            notificationServices.stream()
                    .filter(notificationService ->
                            notificationService.getPreferredContact() == user.getPreference())
                    .findFirst()
                    .ifPresent(notificationService -> notificationService.send(user, text));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
