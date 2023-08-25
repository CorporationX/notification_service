package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.publisher.JsonObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStartEventListener implements MessageListener {

    private final JsonObjectMapper jsonObjectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartEventDto event = jsonObjectMapper.readValue(message.getBody(), EventStartEventDto.class);
    }
}
