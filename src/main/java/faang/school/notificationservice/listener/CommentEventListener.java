package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationServiceResolver;
import faang.school.notificationservice.service.user.FeignUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommentEventListener extends NotificationListener<CommentEvent> {

    public CommentEventListener(List<MessageBuilder<CommentEvent>> builders,
                                NotificationServiceResolver notificationServiceResolver,
                                FeignUserService feignUserService) {
        super(builders, notificationServiceResolver, feignUserService);
    }

    @Override
    protected Long recipientId(CommentEvent event) {
        return event.postAuthorId();
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.comment}",
            groupId = "${spring.kafka.group-id}",
            containerFactory = "jsonKafkaListenerContainerFactory"
    )
    public void onCommentEvent(CommentEvent event) {
        handle(event);
    }
}
