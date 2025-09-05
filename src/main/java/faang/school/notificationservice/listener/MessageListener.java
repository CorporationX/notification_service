package faang.school.notificationservice.listener;

import org.apache.kafka.common.protocol.Message;
import org.springframework.stereotype.Component;

@Component
public interface MessageListener {
    void onMessage(Message message, byte[] pattern);
}
