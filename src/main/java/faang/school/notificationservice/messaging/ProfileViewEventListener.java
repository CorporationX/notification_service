package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final TelegramService telegramService;
    private final MessageBuilderProfileViewEvent messageBuilderProfileViewEvent;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent event = null;
        try {
            event = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String text = messageBuilderProfileViewEvent.buildMessage(event, Locale.getDefault());
        UserDto user = userServiceClient.getUser(event.getViewerId());
        telegramService.send(user,text);
        log.info("Successfully sent a message to (userId: {})", event.getViewerId());
    }
}