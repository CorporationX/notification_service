package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.dto.EventStartDto;
import faang.school.notificationservice.redis.event.EventStartEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventStartEventListener extends AbstractEventListener<EventStartEvent> {
    public EventStartEventListener(UserServiceClientMock userServiceClient,
                                   List<MessageBuilder<?>> messageBuilders,
                                   List<NotificationService> notificationServices,
                                   ObjectMapper objectMapper) {
        super(userServiceClient, messageBuilders, notificationServices, objectMapper, EventStartEvent.class);
    }

    @Override
    protected void processEvent(EventStartEvent event) {
        MessageBuilder<EventStartDto> messageBuilder = (MessageBuilder<EventStartDto>) defineBuilder(EventStartDto.class);

        event.getAttendeeIds().forEach(attendeeId -> {
            UserDto userDto = getUserDto(attendeeId);

            EventStartDto dto = EventStartDto.builder()
                    .eventTitle(event.getTitle())
                    .attendeeName(userDto.getUsername())
                    .build();

            String message = messageBuilder.buildMessage(dto, userDto.getLocale());

            sendNotification(userDto, message);
        });
    }
}
