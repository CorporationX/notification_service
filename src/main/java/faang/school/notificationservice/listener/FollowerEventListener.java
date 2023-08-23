package faang.school.notificationservice.listener;

import org.springframework.data.redis.connection.Message;

public class FollowerEventListener extends AbstractEventListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
