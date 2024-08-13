package faang.school.notificationservice.listener.mentorship;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipAcceptedEvent;
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
class MentorshipAcceptedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<MentorshipAcceptedEvent> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Message redisMessage;
    @InjectMocks
    private MentorshipAcceptedEventListener sut;

    @BeforeEach
    public void setUp() {
        sut = new MentorshipAcceptedEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService)
        );
    }

    @Test
    void onMessage_shouldHandleEventAndSendNotification() throws IOException {
        UUID eventId = UUID.randomUUID();
        MentorshipAcceptedEvent event = new MentorshipAcceptedEvent(eventId,
                1L, 2L, 3L);
        String jsonMessage = String.format(
                "{\"eventId\":\"%s\",\"mentorshipRequestId\":1,\"requesterId\":2,\"receiverId\":3}",
                eventId
        );

        when(redisMessage.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(jsonMessage.getBytes(), MentorshipAcceptedEvent.class)).thenReturn(event);

        String notificationContent = "Mentorship accepted notification";
        when(messageBuilder.getInstance()).thenReturn(MentorshipAcceptedEvent.class);
        when(messageBuilder.buildMessage(event, Locale.US)).thenReturn(notificationContent);

        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(3L)).thenReturn(userDto);

        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        sut.onMessage(redisMessage, new byte[0]);

        verify(objectMapper).readValue(jsonMessage.getBytes(), MentorshipAcceptedEvent.class);
        verify(messageBuilder).getInstance();
        verify(messageBuilder).buildMessage(event, Locale.US);
        verify(userServiceClient).getUser(3L);
        verify(notificationService).getPreferredContact();
        verify(notificationService).send(userDto, notificationContent);
    }

    @Test
    void onMessage_shouldHandleException() throws Exception {
        String invalidJsonMessage = "invalid json";
        when(redisMessage.getBody()).thenReturn(invalidJsonMessage.getBytes());
        when(objectMapper.readValue(invalidJsonMessage.getBytes(), MentorshipAcceptedEvent.class))
                .thenThrow(new IOException("Invalid JSON"));

        assertThrows(EventHandlingException.class, () -> sut.onMessage(redisMessage, new byte[0]));
        verifyNoInteractions(messageBuilder, notificationService, userServiceClient);
    }
}