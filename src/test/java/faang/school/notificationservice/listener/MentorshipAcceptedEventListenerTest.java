package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MentorshipAcceptedMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import faang.school.notificationservice.service.sms.SmsService;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedEventListenerTest {
    private List<NotificationService> notificationServices;

    private List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private SmsService smsService;

    @Mock
    private EmailService emailService;

    @Mock
    private TelegramService telegramService;

    private MentorshipAcceptedMessageBuilder messageBuilder;

    private MentorshipAcceptedEventListener mentorshipAcceptedEventListener;

    private Message message;
    private MentorshipAcceptedEvent mentorshipAcceptedEvent;
    private UserDto userDto;
    private UserDto mentorDto;

    @BeforeEach
    void setUp() {
        message = new DefaultMessage("mentorship_channel".getBytes(),
                "{\"requestId\":1,\"mentorId\":2,\"menteeId\":1}"
                        .getBytes());
        mentorshipAcceptedEvent = new MentorshipAcceptedEvent(1L, 2L, 1L);
        userDto = new UserDto(1, "johndoe", "john@gmail.com",
                "996999060631", UserDto.PreferredContact.SMS);
        mentorDto = new UserDto(2, "mentor", "test@test.com", "996999060632", UserDto.PreferredContact.EMAIL);
        messageBuilder = new MentorshipAcceptedMessageBuilder(messageSource, userServiceClient);
        when(smsService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        notificationServices = List.of(smsService, emailService, telegramService);
        messageBuilders = List.of(messageBuilder);
        mentorshipAcceptedEventListener = new MentorshipAcceptedEventListener(
                notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Test
    @DisplayName("Success event processing with sms sending")
    void mentorshipAcceptedEventListenerTest_successEventProcessingWithSmsSending() throws IOException {
        when(objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class))
                .thenReturn(mentorshipAcceptedEvent);
        when(userServiceClient.getUser(2L)).thenReturn(mentorDto);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("test message");

        mentorshipAcceptedEventListener.onMessage(message, null);
        verify(objectMapper).readValue(message.getBody(), MentorshipAcceptedEvent.class);
        verify(userServiceClient, times(2)).getUser(any(Long.class));
        verify(smsService).send(userDto, "test message");
    }

    @Test
    @DisplayName("Success event processing with email sending")
    void mentorshipAcceptedEventListenerTest_successEventProcessingWithEmailSending() throws IOException {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class))
                .thenReturn(mentorshipAcceptedEvent);
        when(userServiceClient.getUser(2L)).thenReturn(mentorDto);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("test message");

        mentorshipAcceptedEventListener.onMessage(message, null);
        verify(objectMapper).readValue(message.getBody(), MentorshipAcceptedEvent.class);
        verify(userServiceClient, times(2)).getUser(any(Long.class));
        verify(emailService).send(userDto, "test message");
    }

    @Test
    @DisplayName("Success event processing with telegram sending")
    void mentorshipAcceptedEventListenerTest_successEventProcessingWithTelegramSending() throws IOException {
        userDto.setPreference(UserDto.PreferredContact.TELEGRAM);
        when(objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class))
                .thenReturn(mentorshipAcceptedEvent);
        when(userServiceClient.getUser(2L)).thenReturn(mentorDto);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("test message");

        mentorshipAcceptedEventListener.onMessage(message, null);
        verify(objectMapper).readValue(message.getBody(), MentorshipAcceptedEvent.class);
        verify(userServiceClient, times(2)).getUser(any(Long.class));
        verify(telegramService).send(userDto, "test message");
    }

    @Test
    @DisplayName("Object mapper Reading value error")
    void mentorshipAcceptedEventListenerTest_objectMapperReadingValueError() throws IOException {
        when(objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class))
                .thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> mentorshipAcceptedEventListener.onMessage(message, null));
        verify(objectMapper).readValue(message.getBody(), MentorshipAcceptedEvent.class);
    }

    @Test
    @DisplayName("Getting message without message builder")
    void mentorshipAcceptedEventListenerTest_gettingMessageWithoutMessageBuilder() throws IOException {
        messageBuilders = new ArrayList<>();
        notificationServices = new ArrayList<>();
        mentorshipAcceptedEventListener = new MentorshipAcceptedEventListener(
                notificationServices, messageBuilders, userServiceClient, objectMapper);
        when(objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class))
                .thenReturn(mentorshipAcceptedEvent);
        String expectedMessage = "No such message builder found %s"
                .formatted(mentorshipAcceptedEvent.getClass().getName());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> mentorshipAcceptedEventListener.onMessage(message, null));

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    @DisplayName("Getting message without notification service")
    void mentorshipAcceptedEventListenerTest_gettingMessageWithoutNotificationService() throws IOException {
        messageBuilders = List.of(messageBuilder);
        notificationServices = new ArrayList<>();
        mentorshipAcceptedEventListener = new MentorshipAcceptedEventListener(
                notificationServices, messageBuilders, userServiceClient, objectMapper);
        when(objectMapper.readValue(message.getBody(), MentorshipAcceptedEvent.class))
                .thenReturn(mentorshipAcceptedEvent);
        when(userServiceClient.getUser(2L)).thenReturn(mentorDto);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("test message");
        String expectedMessage = "No such notification service found %s"
                .formatted(userDto.getPreference());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> mentorshipAcceptedEventListener.onMessage(message, null));

        assertEquals(expectedMessage, ex.getMessage());
    }

}