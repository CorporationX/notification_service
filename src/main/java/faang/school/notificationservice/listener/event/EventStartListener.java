package faang.school.notificationservice.listener.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.service.EventStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStartListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final EventStartService eventStartService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Optional<EventStartDto> eventStartDto = Optional.empty();

        try {
            eventStartDto = Optional.of(objectMapper.readValue(message.getBody(), EventStartDto.class));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        // temporary - to test publisher
        eventStartDto.ifPresent(System.out::println);
        eventStartDto.ifPresent(eventStartService::sendNotifications);
    }
}
