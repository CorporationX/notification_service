package faang.school.notificationservice.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class FolowerEventListener extends AbstractEventListener<FollowerEventDto> {


    public FolowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServices, List<MessageBuilder<FollowerEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEventDto followerEventDto = convertToJSON(message, FollowerEventDto.class);
        String messageToSend = getMessage(followerEventDto, Locale.ENGLISH);
        sendNotification(followerEventDto.getFolloweeId(), messageToSend);
    }
}
