package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.service.EventStartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStartListener implements MessageListener {
    private final EventStartService eventStartService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        EventStartDto eventStartDto;

        try {
            eventStartDto = objectMapper.readValue(messageBody, EventStartDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        eventStartService.scheduleNotifications(eventStartDto);
    }
}
