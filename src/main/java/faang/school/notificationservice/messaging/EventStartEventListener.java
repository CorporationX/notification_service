package faang.school.notificationservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.EventListener;

@Component
@RequiredArgsConstructor
public class EventStartEventListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
