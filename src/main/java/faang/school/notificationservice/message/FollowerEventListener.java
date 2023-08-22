package faang.school.notificationservice.message;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
