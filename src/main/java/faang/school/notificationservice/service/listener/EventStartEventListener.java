package faang.school.notificationservice.service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.notificationservice.dto.EventStartEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventStartEventListener implements MessageListener {
    private final ObjectMapper objectMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartEvent eventStartEvent;
        try {

           eventStartEvent = objectMapper.readValue( message.getBody(), EventStartEvent.class );

        } catch (IOException e) {
            log.warn( "Unsuccessful mapping", e );
            throw new RuntimeException( e );
        }


        log.info( "Data successfully passed to analyticsEventService" );

    }
}
