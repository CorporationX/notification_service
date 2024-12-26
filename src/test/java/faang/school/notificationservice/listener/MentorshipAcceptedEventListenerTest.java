package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.RetryProperties;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.MentorshipAcceptedMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RetryProperties retryProperties;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private MentorshipAcceptedMessageBuilder messageBuilder;

    @InjectMocks
    private MentorshipAcceptedEventListener mentorshipAcceptedEventListener;

    private byte[] body;

    @BeforeEach
    void setUp() {
        mentorshipAcceptedEventListener = new MentorshipAcceptedEventListener(
                objectMapper,
                retryProperties,
                userServiceClient,
                List.of(emailNotificationService),
                messageBuilder
        );

        body = "{\"mentorshipRequestId\":1,\"description\":\"Request\",\"receiverId\":2,\"receiverUserName\":\"John\",\"requesterId\":3,\"requesterUserName\":\"Mark\"}"
                .getBytes(StandardCharsets.UTF_8);
    }

    @Test
    void onMessageSuccessfully() throws Exception {
        String expectedMessage = "Your request has been accepted";
        MentorshipAcceptedEvent event = new MentorshipAcceptedEvent(1L, "Request", 2L, "John", 3L,"Mark");

        UserContactsDto userContactsDto = new UserContactsDto();
        userContactsDto.setId(2L);
        userContactsDto.setPreference(NotificationChannel.EMAIL);

        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(body);

        when(objectMapper.readValue(body, MentorshipAcceptedEvent.class)).thenReturn(event);
        when(userServiceClient.getUserContacts(2L)).thenReturn(userContactsDto);
        when(emailNotificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);
        when(messageBuilder.buildMessage(event, Locale.getDefault())).thenReturn(expectedMessage);

        mentorshipAcceptedEventListener.onMessage(redisMessage, null);

        ArgumentCaptor<UserContactsDto> userCaptor = ArgumentCaptor.forClass(UserContactsDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailNotificationService, times(1)).send(userCaptor.capture(), messageCaptor.capture());

        assertEquals(2L, userCaptor.getValue().getId());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }
}