package faang.school.notificationservice.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public abstract class AbstractEventListener implements MessageListener {

    public void onMessage(Message message, byte[] pattern) {

    }
}
