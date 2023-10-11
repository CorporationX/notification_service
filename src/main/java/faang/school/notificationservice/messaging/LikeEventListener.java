package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipOfferRequestSentDto;
import faang.school.notificationservice.dto.like.LikeEventDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class LikeEventListener extends EventListenerBase<LikeEventDto> implements MessageListener {

    public LikeEventListener(JsonMapper jsonMapper,
                                          UserServiceClient userServiceClient,
                                          List<MessageBuilder<LikeEventDto>> messageBuilders,
                                          List<NotificationService> notificationServices) {
        super(jsonMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        jsonMapper.toObject(message.toString(), LikeEventDto.class)
                .ifPresent(like -> {
                    String text = getMessage(like, Locale.ENGLISH);
                    sendNotification(like.getUserId(), text);
                });
        log.info(message + " " + "send");
    }
}