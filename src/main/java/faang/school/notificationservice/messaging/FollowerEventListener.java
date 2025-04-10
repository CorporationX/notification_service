package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@RedisListenerChannels({"follower_channel"})
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            objectMapper.readValue(message.getBody(), UserDto.class);
        } catch (IOException exception) {
            log.error("Error while deserializing message", exception);
            throw new RuntimeException("Error while deserializing message");
        }
    }
}
