package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class EventStartEventListener extends AbstractEventListener<EventDto> implements MessageListener {

    public EventStartEventListener(ObjectMapper objectMapper, UserServiceClient userService,
                                   List<NotificationService> services, MessageBuilder<EventDto> messageBuilder) {
        super(objectMapper, userService, services, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventDto eventDto = handleEvent(message, EventDto.class);
        log.debug("Handle Event: {}", eventDto);
        String text = getMessage(eventDto, Locale.ENGLISH);
        log.debug("Message: {}", text);
        List<Long> userIds = eventDto.usersId();

        for (Long userId : userIds) {
            try {
                sendNotification(userId, text);
                log.debug("Send notification for user id : {}", userId);
            } catch (Exception e) {
                log.error("Error while sending notification for user id : {}", userId, e);
            }
        }
    }
}