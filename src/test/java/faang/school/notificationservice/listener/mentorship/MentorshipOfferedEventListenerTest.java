package faang.school.notificationservice.listener.mentorship;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.request.MentorshipOfferedEvent;
import faang.school.notificationservice.exception.listener.EventHandlingException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipOfferedEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<MentorshipOfferedEvent> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Message redisMessage;
    @InjectMocks
    private MentorshipOfferedEventListener mentorshipOfferedEventListener;

    @BeforeEach
    void setUp() {
        mentorshipOfferedEventListener = new MentorshipOfferedEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService)
        );
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        UUID eventId = UUID.randomUUID();
        MentorshipOfferedEvent event =
                new MentorshipOfferedEvent(eventId, 1L, 2L, 3L);
        String jsonMessage = String.format(
                "{\"eventId\":\"%s\",\"mentorshipOfferId\":1,\"requesterId\":2,\"receiverId\":3}",
                eventId);
        when(redisMessage.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(jsonMessage.getBytes(), MentorshipOfferedEvent.class))
                .thenReturn(event);

        String notificationContent = "Mentorship offered notification";
        when(messageBuilder.getInstance()).thenReturn(MentorshipOfferedEvent.class);
        when(messageBuilder.buildMessage(event, Locale.US)).thenReturn(notificationContent);

        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(3L)).thenReturn(userDto);

        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        mentorshipOfferedEventListener.onMessage(redisMessage, new byte[0]);

        verify(objectMapper).readValue(jsonMessage.getBytes(), MentorshipOfferedEvent.class);
        verify(messageBuilder).getInstance();
        verify(messageBuilder).buildMessage(event, Locale.US);
        verify(userServiceClient).getUser(3L);
        verify(notificationService).getPreferredContact();
        verify(notificationService).send(userDto, notificationContent);
    }

    @Test
    void testOnMessageException() throws Exception {
        String invalidJsonMessage = "invalid json";
        when(redisMessage.getBody()).thenReturn(invalidJsonMessage.getBytes());
        when(objectMapper.readValue(invalidJsonMessage.getBytes(), MentorshipOfferedEvent.class))
                .thenThrow(new IOException("Invalid JSON"));

        assertThrows(EventHandlingException.class, () -> mentorshipOfferedEventListener.onMessage(redisMessage, new byte[0]));
        verifyNoInteractions(messageBuilder, notificationService, userServiceClient);
    }
}
