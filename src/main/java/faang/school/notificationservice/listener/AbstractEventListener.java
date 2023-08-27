package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final List<NotificationService> services;
    protected final List<MessageBuilder> messageBuilders;
    protected final UserServiceClient userService;
    protected final JsonObjectMapper jsonObjectMapper;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        T event = jsonObjectMapper.readValue(message.getBody(), type);
        consumer.accept(event);
    }

    protected void sendMessage(T eventDto, long userId) {
        UserDto userDto = userService.getUser(userId);

        String message = messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getEventType() == eventDto.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(eventDto, userDto))
                .get();

        services.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .ifPresent(service -> service.sendNotification(message, userDto));
    }

}