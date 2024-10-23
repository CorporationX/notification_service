package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component("eventStartEventListener")
public class EventStartEventListener extends AbstractEventListener<EventDto> {

    public EventStartEventListener(List<NotificationService> notificationServices,
                                   List<MessageBuilder<EventDto>> messageBuilders,
                                   UserServiceClient userServiceClient,
                                   ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventDto eventDto = handleEvent(message, EventDto.class);
        log.debug("Handle Event: {}", eventDto);
        String text = getMessage(eventDto, Locale.ENGLISH);
        log.debug("Message: {}", text);
        List<Long> userIds = eventDto.userIds();

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