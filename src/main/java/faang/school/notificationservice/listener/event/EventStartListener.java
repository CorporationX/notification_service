package faang.school.notificationservice.listener.event;

import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.mapper.JsonMapper;
import faang.school.notificationservice.service.EventStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStartListener implements MessageListener {
    private final JsonMapper<EventStartDto> jsonMapper;
    private final EventStartService eventStartService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartDto eventStartDto = jsonMapper.toObject(message.getBody(), EventStartDto.class);

        log.info("Sending notifications for event " + eventStartDto.getId());
        eventStartService.scheduleNotifications(eventStartDto);
    }
}
