package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.event.GoalCompletedEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoalCompletedEventListener implements MessageListener{

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}

        //extends AbstractEventListener<GoalCompletedEventDto>
