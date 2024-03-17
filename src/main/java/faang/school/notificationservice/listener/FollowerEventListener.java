package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.GoalDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;


@Component
@Slf4j
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> {

    public FollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices, List<MessageBuilder> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEventDto event = getEvent(message, FollowerEventDto.class);
        log.info("Start processing an incoming event - {}", event);

        UserDto follower = userServiceClient.getUser(event.getFollowerId());
        UserDto followee = userServiceClient.getUser(event.getFolloweeId());
        event.setFollowerId(follower.getId());
        event.setFolloweeId(followee.getId());
        String messageToSend = getMessage(event, Locale.getDefault());
        sendNotification(followee, messageToSend);
        log.info("The message: {} has been sent to {}", messageToSend, followee.getUsername());
    }
}