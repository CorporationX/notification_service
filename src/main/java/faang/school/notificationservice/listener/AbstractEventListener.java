package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T, V>{
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T, V>> messageBuilders;

    public String getMessage(Class<?> eventType, V locale, T event) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getEventType().equals(eventType))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(IllegalArgumentException::new);
    }

    public void sendNotification(long userId, String message) {
//        UserDto userDto = userServiceClient.getUser(userId);
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("some@email.com");
        userDto.setPreference(UserDto.PreferredContact.SMS);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .send(userDto, message);
    }
}
