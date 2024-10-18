package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.event.SkillAcquiredEvent;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class SkillAcquiredEventListener extends AbstractEventListener<SkillAcquiredEvent> implements MessageListener {

    private final UserServiceClient userServiceClient;

    public SkillAcquiredEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, notificationServices, messageBuilders);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, SkillAcquiredEvent.class, event -> {
            UserDto beneficiaryDto = userServiceClient.getUser(event.getUserId());
            String notificationMessage = buildMessage(event, Locale.UK);
            sendNotification(beneficiaryDto, notificationMessage);
            log.info("Notification was sent, receiverId: {}, notificationMessage: {}", beneficiaryDto.getId(), notificationMessage);
        });
    }
}
