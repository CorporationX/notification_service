package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener <T> {
    protected final ObjectMapper mapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final JavaMailSender javaMailSender;
    private final List<NotificationService> notificationServices;

    protected void handleEvent(Message message, Class<T> eventType, Consumer consumer){
        T event = null;
        try {
            event = mapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        consumer.accept(event);
    }

    protected String getMessage(T event, Locale userLocale){
        return messageBuilders.stream()
                .filter((messageBuilder) -> messageBuilder.supportsEventType(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, Locale.UK))
                .orElseThrow(() -> new EntityNotFoundException("No message builder is found given event type: " + event.getClass().getName()));
    }

    protected void sendNotification (Long userId, String message){
        UserDto userDto = userServiceClient.getNotificationUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == userDto.getContactPreference())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Preferred service not found"))
                .send(userDto, message);
    }
}
