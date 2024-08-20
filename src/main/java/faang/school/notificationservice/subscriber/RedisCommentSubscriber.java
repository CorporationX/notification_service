package faang.school.notificationservice.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisCommentSubscriber implements MessageListener {
    public static List<String> eventList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        eventList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
    }
}
