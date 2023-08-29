package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final JsonObjectMapper jsonObjectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServiceList;

    protected String getMessage(T event, UserDto userDto){
        return messageBuilders.stream()
                .filter(builder -> builder.getEventType() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userDto))
                .orElseThrow(()-> new IllegalArgumentException("No message builder was found for event type: " + event.getClass().getName()));
    }

}
