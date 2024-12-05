package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.TelegramService;
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
    private final TelegramService telegramService;
    private final LikeMessageBuilder likeMessageBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            json = json.replaceAll("\"@class\".*?,", "");

            LikeEvent event = objectMapper.readValue(json, LikeEvent.class);
            telegramService.send(
                    UserDto.builder().id(event.getLikeAuthorId()).preference(UserDto.PreferredContact.TELEGRAM).build(),
                    likeMessageBuilder.buildMessage(event, Locale.US)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

