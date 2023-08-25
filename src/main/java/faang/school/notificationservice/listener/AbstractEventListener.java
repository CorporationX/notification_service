package faang.school.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener implements MessageListener {

}
