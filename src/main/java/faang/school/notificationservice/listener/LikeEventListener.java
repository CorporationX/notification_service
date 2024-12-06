package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final EmailService emailService;
    private final LikeMessageBuilder likeMessageBuilder;
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            json = json.replaceAll("\"@class\".*?,", "");

            LikeEvent event = objectMapper.readValue(json, LikeEvent.class);

            UserDto user = userServiceClient.getUser(event.getPostAuthorId());
            user.setPreference(UserDto.PreferredContact.EMAIL);

            emailService.send(
                    user,
                    likeMessageBuilder.buildMessage(event, Locale.US)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

