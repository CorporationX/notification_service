package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEventListener {

    private final List<NotificationService> services;
    private final List<MessageBuilder> messageBuilders;
    private final UserServiceClient userService;

    public void sendMessage(EventDto eventDto) {
        UserDto userDto = userService.getUser(eventDto.getUserId());

        String message = messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getEventType() == eventDto.getEventType())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(userDto, eventDto))
                .get();

        services.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .ifPresent(service -> service.sendNotification(message, userDto));
    }

}
