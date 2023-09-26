package faang.school.notificationservice.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEventDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEventDto> {

    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        LikeEventDto likeEventDto = convertToJSON(message, LikeEventDto.class);
        String messageToSend = getMessage(likeEventDto, Locale.ENGLISH);
        sendNotification(likeEventDto.getReceiverId(), messageToSend);
    }
}
