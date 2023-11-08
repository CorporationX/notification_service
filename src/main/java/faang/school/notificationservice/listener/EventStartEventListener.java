package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class EventStartEventListener extends AbstractEventListener<EventDto> {
    public EventStartEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<NotificationService> notificationServices,
                                   List<MessageBuilder<Class<?>>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventDto eventDto = convertToJSON(message, EventDto.class);
        String messageToSend = getMessage(eventDto.getClass(), Locale.ENGLISH);
        List<UserDto> userDtoList= userServiceClient.getParticipantEvent(eventDto.getId());
        for(UserDto user:userDtoList){
            preSendNotificationEvent(eventDto, user, messageToSend);
        }
    }
}
