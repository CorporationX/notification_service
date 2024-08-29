package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.CommentEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommentEventListener extends AbstractListener<CommentEvent> {

    public CommentEventListener(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public CommentEvent handleEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), CommentEvent.class);
    }

}
