package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilderList;
    private final List<NotificationService> notificationServicesList;

    protected void   handleEvent(Message message, Class<T> type, Consumer<T> consumer){
        try {
            T profileViewDto = objectMapper.readValue(message.getBody(), type);
            consumer.accept(profileViewDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event){
        return messageBuilderList.stream()
                .filter(tMessageBuilder -> tMessageBuilder.supportEventType() == event.getClass())
                .findFirst()
                .map(message -> message.buildMessage(event, Locale.UK))
                .orElseThrow(()-> new IllegalArgumentException("Message not found" + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServicesList.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification service not Found"))
                .send(userDto, message);
    }
}
