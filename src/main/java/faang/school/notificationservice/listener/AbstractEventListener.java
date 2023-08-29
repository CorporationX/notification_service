package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final UserServiceClient userService;
    protected final JsonObjectMapper jsonObjectMapper;
    protected final List<NotificationService> services;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        T event = jsonObjectMapper.readValue(message.getBody(), type);
        consumer.accept(event);
    }

    protected void sendMessage(T eventDto, long receiverId, long actorId) {
        UserDto receiverDto = userService.getUser(receiverId);
        UserDto actorDto;

        if (actorId == receiverId) {
            actorDto = receiverDto;
        } else {
            actorDto = userService.getUser(actorId);
        }

        String message = messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getEventType() == eventDto.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(actorDto, eventDto))
                .get();

        services.stream()
                .filter(service -> service.getPreferredContact() == receiverDto.getPreference())
                .findFirst()
                .ifPresent(service -> service.sendNotification(message, receiverDto));
    }
}
