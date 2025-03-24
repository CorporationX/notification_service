package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.handler.EventHandler;
import faang.school.notificationservice.handler.KafkaMapperHandler;
import faang.school.notificationservice.handler.MessageHandler;
import faang.school.notificationservice.handler.NotificationServiceHandler;
import faang.school.notificationservice.handler.UserServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    private final UserServiceHandler userServiceHandler;

    public LikeEventListener(EventHandler eventHandler,
                             NotificationServiceHandler notificationServiceHandler,
                             MessageHandler<LikeEvent> messageHandler,
                             KafkaMapperHandler kafkaMapperHandler,
                             UserServiceHandler userServiceHandler) {
        super(eventHandler, notificationServiceHandler, messageHandler, kafkaMapperHandler);
        this.userServiceHandler = userServiceHandler;
    }

    @KafkaListener(topics = "${user-like-post.topic-name}")
    public void listen(ConsumerRecord<String, Object> kafkaEvent) {
        LikeEvent inputDto = handleUniqueEvent(kafkaEvent, LikeEvent.class);


        UserServiceDto postAuthor = userServiceHandler.getSingleUser(inputDto.getAuthorId());
        UserServiceDto liker =  userServiceHandler.getSingleUser(inputDto.getUserId());

        List<String> additionalWordsForOwnerMessage = List.of(liker.getUsername());
        String message = getMessage(inputDto, postAuthor, additionalWordsForOwnerMessage);

        log.info("sending message {}", message);
        sendSingleNotification(postAuthor, message);
    }

}
