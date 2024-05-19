package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillOfferedEvent;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.Locale;

@Configuration
@Slf4j
public class SkillOfferedEventListener extends AbstractEventListener<SkillOfferedEvent>{

    public SkillOfferedEventListener(MessageBuilder<SkillOfferedEvent> messageBuilder,
                                     List<NotificationService> notificationServices,
                                     UserServiceClient userServiceClient,
                                     ObjectMapper objectMapper) {
        super(messageBuilder, notificationServices, userServiceClient, objectMapper);
    }

    @KafkaListener(topics = "${spring.data.kafka.channels.skill-channel.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(String message){
        try {
            SkillOfferedEvent event = objectMapper.readValue(message, SkillOfferedEvent.class);
            sendNotification(event.getRecipientUserId(), getMessage(event, Locale.ENGLISH));
            log.info(getMessage(event, Locale.ENGLISH));
            log.info("Sending notification in listener for user: " + event.getRecipientUserId());
        } catch (JsonProcessingException e) {
            log.error("error in json mapping", e);
            throw new RuntimeException(e);
        }
    }
}
