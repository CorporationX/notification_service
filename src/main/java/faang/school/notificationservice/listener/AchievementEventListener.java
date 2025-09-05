package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.apache.kafka.common.protocol.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> implements MessageListener {
    private ObjectMapper objectMapper;
    private UserServiceClient userServiceClient;

    public AchievementEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                    List<MessageBuilder<AchievementEvent>> messageBuilders,
                                    List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
