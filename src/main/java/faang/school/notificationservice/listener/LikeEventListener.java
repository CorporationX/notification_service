package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.data.PreferredContact;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final LikeMessageBuilder likeMessageBuilder;
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            LikeEvent event = objectMapper.readValue(json, LikeEvent.class);

            UserContactsDto user = userServiceClient.getUserContacts(event.getPostAuthorId());

            notificationServices.stream()
                    .filter(service -> user.getPreference().equals(service.getPreferredContact()))
                    .findFirst()
                    .ifPresent(service -> service.send(user, likeMessageBuilder.buildMessage(event, Locale.US)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

