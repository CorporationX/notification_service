package faang.school.notificationservice.listener;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStartEventListener implements MessageListener {

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        System.out.println("Event with ID " + message.toString() + " has just started!");
    }
}
