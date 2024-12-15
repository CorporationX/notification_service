package faang.school.notificationservice.eventlistener.mentorship;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.mentorship.MentorshipAcceptedEvent;
import faang.school.notificationservice.listener.mentorship.MentorshipAcceptedEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    private MentorshipAcceptedEventListener eventListener;
    private List<NotificationService> notificationServices;
    private List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        messageBuilders = new ArrayList<>();
        MessageBuilder<MentorshipAcceptedEvent> mockedMessageBuilder = mock(MessageBuilder.class);
        when(mockedMessageBuilder.getInstance()).thenReturn(MentorshipAcceptedEvent.class);
        messageBuilders.add(mockedMessageBuilder);

        notificationServices = new ArrayList<>();
        notificationServices.add(notificationService);

        eventListener = new MentorshipAcceptedEventListener(objectMapper,
                userServiceClient,
                notificationServices,
                messageBuilders);
    }

    @Test
    void testOnMessageSuccess() throws Exception {
        MentorshipAcceptedEvent event = prepareEvent();
        UserDto user = prepareUser();

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);

        when(userServiceClient.getUser(2L)).thenReturn(user);
        when(objectMapper.readValue(messageBody, MentorshipAcceptedEvent.class)).thenReturn(event);
        when(messageBuilders.get(0).buildMessage(event, Locale.getDefault())).thenReturn("Test message");
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.onMessage(message, null);

        verify(notificationService).send(any(UserDto.class), eq("Test message"));
    }

    @Test
    void testGetMessageNoNotificationServiceFound() throws Exception {
        MentorshipAcceptedEvent event = prepareEvent();
        UserDto user = prepareUser();

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);

        when(userServiceClient.getUser(2L)).thenReturn(user);
        when(objectMapper.readValue(messageBody, MentorshipAcceptedEvent.class)).thenReturn(event);
        when(messageBuilders.get(0).buildMessage(event, Locale.getDefault())).thenReturn("Test message");
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(IllegalArgumentException.class, () -> eventListener.onMessage(message, null));
    }

    @Test
    void testGetMessageNoMessageBuilderFound() throws Exception {
        MentorshipAcceptedEvent event = prepareEvent();

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);

        when(objectMapper.readValue(messageBody, MentorshipAcceptedEvent.class)).thenReturn(event);

        assertThrows(IllegalArgumentException.class, () -> eventListener.onMessage(message, null));
    }

    private MentorshipAcceptedEvent prepareEvent() {
        return new MentorshipAcceptedEvent(
                1L,
                2L,
                3L,
                LocalDateTime.of(2024, 12, 5, 15, 41, 16)
        );
    }

    private UserDto prepareUser() {
        return UserDto.builder()
                .id(3L)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
}
