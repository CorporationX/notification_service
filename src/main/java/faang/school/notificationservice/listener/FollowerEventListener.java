package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final FollowerMessageBuilder followerMessageBuilder;
    private final List<NotificationService> notificationServices;
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Locale locale = Locale.ENGLISH;
        try {
            String messageBody = new String(message.getBody());
            log.info("Received message: {}", messageBody);
            FollowerEvent followerEvent = objectMapper.readValue(messageBody, FollowerEvent.class);
            String messageText = followerMessageBuilder.buildMessage(followerEvent, locale);
            UserDto user = userServiceClient.getUser(followerEvent.getSubscriberId());
            notificationServices.forEach(notificationService -> notificationService.send(user, messageText));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


}
