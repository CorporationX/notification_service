package faang.school.notificationservice.service.listener;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.dto.kafka.FollowUserDto;
import faang.school.notificationservice.handler.EventHandler;
import faang.school.notificationservice.handler.KafkaMapperHandler;
import faang.school.notificationservice.handler.MessageHandler;
import faang.school.notificationservice.handler.NotificationServiceHandler;
import faang.school.notificationservice.handler.UserServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class FollowUserEventListener extends AbstractEventListener<FollowUserDto> {
    private final UserServiceHandler userServiceHandler;

    public FollowUserEventListener(EventHandler eventHandler,
                                     NotificationServiceHandler notificationServiceHandler,
                                     MessageHandler<FollowUserDto> messageHandler,
                                     KafkaMapperHandler kafkaMapperHandler,
                                     UserServiceHandler userServiceHandler) {
        super(eventHandler, notificationServiceHandler, messageHandler, kafkaMapperHandler);
        this.userServiceHandler = userServiceHandler;
    }

    @KafkaListener(topics = "${user-follows-user.topic-name}")
    public void listenTopic(ConsumerRecord<String, Object> event) {
        FollowUserDto inputDto = handleUniqueEvent(event, FollowUserDto.class);
        UserServiceDto followeeDto = userServiceHandler.getSingleUser(inputDto.followeeId());
        UserServiceDto followerDto = userServiceHandler.getSingleUser(inputDto.followerId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        String formattedDate = inputDto.followedAt().format(formatter);
        List<String> followerMessageToFollowee = List.of(followerDto.getUsername(), formattedDate);

        String message = getMessage(inputDto, followeeDto, followerMessageToFollowee);

        log.info("Sending message {} to user {}", message, followeeDto.getUsername());
        sendSingleNotification(followeeDto, message);
    }
}
