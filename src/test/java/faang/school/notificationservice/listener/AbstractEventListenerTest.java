package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<FollowerEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    private static final long FOLLOWEE_ID = 1L;

    private static final FollowerEvent EVENT =
            new FollowerEvent(FOLLOWEE_ID, 2L, LocalDateTime.now());

    @Test
    void shouldHandleEvent_whenEverythingIsConfigured() throws Exception {
        when(messageBuilder.getInstance())
                .thenReturn(FollowerEvent.class);

        when(notificationService.getPreferredContact())
                .thenReturn(UserDto.PreferredContact.EMAIL);

        FollowerEventListener listener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                List.of(messageBuilder),
                List.of(notificationService)
        );

        UserDto followee = new UserDto();
        followee.setId(FOLLOWEE_ID);
        followee.setUsername("user");
        followee.setEmail("user@gmail.com");
        followee.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(FOLLOWEE_ID))
                .thenReturn(followee);

        String builtMessage = "test-message to user";

        when(messageBuilder.buildMessage(EVENT, Locale.ENGLISH))
                .thenReturn(builtMessage);

        Message convertedEvent = mockMessage(EVENT);

        listener.onMessage(convertedEvent, null);

        verify(messageBuilder).buildMessage(EVENT, Locale.ENGLISH);
        verify(userServiceClient).getUser(FOLLOWEE_ID);
        verify(notificationService).send(followee, builtMessage);
    }

    @Test
    void shouldThrowException_whenMessageBuilderNotFound() throws Exception {
        when(notificationService.getPreferredContact())
                .thenReturn(UserDto.PreferredContact.EMAIL);

        FollowerEventListener listener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                List.of(),
                List.of(notificationService)
        );

        Message message = mockMessage(EVENT);

        assertThrows(IllegalArgumentException.class,
                () -> listener.onMessage(message, null)
        );
    }

    @Test
    void shouldThrowException_whenNotificationServiceNotFound() throws Exception {
        when(messageBuilder.getInstance())
                .thenReturn(FollowerEvent.class);

        FollowerEventListener listener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                List.of(messageBuilder),
                List.of()
        );

        UserDto followee = new UserDto();
        followee.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(FOLLOWEE_ID))
                .thenReturn(followee);

        Message convertedEvent = mockMessage(EVENT);

        assertThrows(IllegalArgumentException.class,
                () -> listener.onMessage(convertedEvent, null)
        );
    }

    private Message mockMessage(FollowerEvent event) throws Exception {
        Message message = mock(Message.class);
        byte[] convertedEvent = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(convertedEvent);
        return message;
    }
}
