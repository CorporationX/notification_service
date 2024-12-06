package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.AchievementEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> {

    @Value("${spring.data.redis.channel.achievement}")
    private String topicName;

    @Autowired
    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationService,
                                    List<MessageBuilder<AchievementEvent>> messageBuilder) {
        super(objectMapper, userServiceClient, notificationService, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, AchievementEvent.class, event -> {
            String messageText = getMessage(event, Locale.ENGLISH);
            sendNotification(event.getUserId(), messageText);
        });
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(topicName);
    }
}
