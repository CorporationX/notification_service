package faang.school.notificationservice.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RedisCommentSubscriber implements MessageListener {
    public final List<String> eventList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        eventList.add(message.toString());
        log.info("Message received from channel: {}, body message: {}", message.getChannel(), message.getBody());
    }
}
