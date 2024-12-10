package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.comment.CommentEventDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {

    @Value("${spring.data.redis.channel.comment}")
    private String commentTopic;

    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationService,
                                List<MessageBuilder<CommentEventDto>> messageBuilder) {
        super(objectMapper, userServiceClient, notificationService, messageBuilder);
    }

    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic(commentTopic);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, CommentEventDto.class, commentEventDto -> {
            String text = getMessage(commentEventDto, Locale.ENGLISH);
            sendNotification(commentEventDto.getPostCreatorId(), text);
        });
    }
}
