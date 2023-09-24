package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class FollowerEventListener extends AbstractEventListener<FollowerDto> {
    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<MessageBuilder<FollowerDto>> messageBuilders,
                                 List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerDto followerDto = readValue(message.getBody(), FollowerDto.class);
        UserDto followee = userServiceClient.getUser(followerDto.getFollowerId());
        String messageText = getMessage(followerDto, Locale.getDefault());
        sendNotification(followee, messageText);
        log.info("Sending notifications for follower: {}", followerDto);
    }


}
