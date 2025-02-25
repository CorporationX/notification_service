package faang.school.notificationservice.kafka.listener.comment;

import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.kafka.listener.EventListener;
import faang.school.notificationservice.service.comment.CommentService;
import faang.school.notificationservice.utils.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EvilCommenterEventListener implements EventListener {

    private final CommentService commentService;
    private final EventMapper<CommentEvent> eventMapper;

    @KafkaListener(topics = "${kafka.comment.topic}", groupId = "%{spring.kafka.group.id}")
    @Override
    public void listen(String message) {
        CommentEvent commentEvent = eventMapper.mapMessageToEvent(message, CommentEvent.class);
        log.info("Received comment event: {}", commentEvent);
        commentService.sendNotification(commentEvent);
    }
}
