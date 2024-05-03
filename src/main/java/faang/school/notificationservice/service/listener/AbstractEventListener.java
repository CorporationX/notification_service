package faang.school.notificationservice.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener{
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected abstract Class<T> getEventType();
    protected abstract List<Long> getAttendeeIds(T event);
    @Override
    public void onMessage(Message message, byte[] pattern) {
        ExecutorService service = Executors.newFixedThreadPool(4);
        try {
            T event = objectMapper.readValue(message.getBody(), getEventType());
            List<Long> attendees = getAttendeeIds(event);
            System.out.println(attendees);
            attendees.forEach(attendee -> service.submit(() -> notifyAttendees(attendee, getMessage(event, Locale.US))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getMessage(T event, Locale userLocale){

        return messageBuilders.stream()
                .filter(messageBuilder->messageBuilder.supportsEventType(event.getClass()))
                .findFirst()
                .map(messageBuilder->messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(()->new IllegalArgumentException("No MessageBuilder found for event type: " + event.getClass().getName()));

    }
    public void notifyAttendees(Long id, String message){
        UserDto user=userServiceClient.getUser(id);

        notificationServices.stream()
                .filter(notificationService->notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("No notification service found for the user's preferred communication method."))
                .send(user, message);
    }
}