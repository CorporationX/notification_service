package faang.school.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //TODO реализовать
    }
}
