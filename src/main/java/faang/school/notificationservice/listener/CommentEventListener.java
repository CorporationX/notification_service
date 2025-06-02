package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.event.CommentEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class CommentEventListener extends AbstractListener<CommentEventDto> {

    public CommentEventListener() {
        super(CommentEventDto.class);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topics.COMMENT_CREATED}",
            groupId = "${spring.kafka.consumer.groupId}")
    public void handle(CommentEventDto event) {
        log.info("Comment event received: {}", event);
        sendNotification(event.getPostAuthorId(), getMessage(event, Locale.ENGLISH));
    }
}