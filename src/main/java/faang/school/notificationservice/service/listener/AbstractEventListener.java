package faang.school.notificationservice.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.EventStartMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    public String getMessage(T event, Locale userLocale) {
        if (event instanceof EventStartEvent) {
            return messageBuilders.stream()
                    .filter(messageBuilder -> messageBuilder instanceof EventStartMessageBuilder )
                    .findFirst()
                    .map(messageBuilder -> ((EventStartMessageBuilder) messageBuilder).buildMessage((EventStartEvent) event, userLocale))
                    .orElseThrow(() -> new IllegalArgumentException("No EventStartMessageBuilder found"));
        } else {
            throw new IllegalArgumentException("Unsupported event type: " + event.getClass().getName());
        }
    }

    public void notifyAttendees(Long id, String message) {
        UserDto user = userServiceClient.getUser( id );

        notificationServices.stream()
                .filter( notificationService -> notificationService.getPreferredContact().equals( user.getPreference() ) )
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException( "No notification service found for the user's preferred communication method." ) )
                .send( user, message );
    }
}