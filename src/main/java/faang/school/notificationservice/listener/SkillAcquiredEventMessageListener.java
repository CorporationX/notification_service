package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.skill.SkillAcquiredEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;


@Component
public class SkillAcquiredEventMessageListener extends AbstractEventListener<SkillAcquiredEvent> {

    public SkillAcquiredEventMessageListener(ObjectMapper objectMapper,
                                             UserServiceClient userServiceClient,
                                             Map<Class<?>, MessageBuilder<?>> messageBuilders,
                                             Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, SkillAcquiredEvent.class, skillAcquiredEvent -> {
            UserDto userDto = userServiceClient.getUser(skillAcquiredEvent.getReceiverId());
            String textMessage = getMessage(skillAcquiredEvent, Locale.UK);
            sendNotification(userDto, textMessage);
        });
    }
}
