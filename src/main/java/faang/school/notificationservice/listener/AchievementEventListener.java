package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.AchievementEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class AchievementEventListener extends AbstractListener<AchievementEventDto> {

    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<MessageBuilder<AchievementEventDto>> messageBuilders,
                                    List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(topics = "writer_achieved_topic", groupId = "not-service")
    public void handle(AchievementEventDto event) {
        log.info("Start handle achievement event {}", event);
        sendNotification(event.getUserId(), getMessage(event, Locale.ENGLISH));
    }
}
