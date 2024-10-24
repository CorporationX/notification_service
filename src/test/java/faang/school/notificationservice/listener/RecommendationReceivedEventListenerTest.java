package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.RecommendationReceivedMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import faang.school.notificationservice.service.sms.SmsService;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecommendationReceivedEventListenerTest {

    private List<NotificationService> notificationServices;

    private List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders;

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

    private RecommendationReceivedMessageBuilder messageBuilder;

    private RecommendationReceivedEventListener recommendationReceivedEventListener;

    private Message message;
    private RecommendationReceivedEvent recommendationReceivedEvent;
    private UserDto recipientUser;
    private UserDto authorUser;

    @BeforeEach
    void setUp() {
        message = new DefaultMessage("recommendation_channel".getBytes(),
                "{\"recommendationId\":1,\"authorId\":2,\"recipientId\":3}".getBytes());
        recommendationReceivedEvent = new RecommendationReceivedEvent(1L, 2L, 3L);

        recipientUser = new UserDto(3L, "recipientUser", "recipient@example.com",
                "1234567890", UserDto.PreferredContact.EMAIL);
        authorUser = new UserDto(2L, "authorUser", "author@example.com",
                "0987654321", UserDto.PreferredContact.SMS);

        messageBuilder = new RecommendationReceivedMessageBuilder(messageSource, userServiceClient);

        when(smsService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        notificationServices = List.of(smsService, emailService, telegramService);
        messageBuilders = List.of(messageBuilder);

        recommendationReceivedEventListener = new RecommendationReceivedEventListener(
                notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Test
    @DisplayName("Success event processing with Email sending")
    void recommendationReceivedEventListenerTest_successEventProcessingWithEmailSending() throws IOException {
        recipientUser.setPreference(UserDto.PreferredContact.EMAIL);

        when(objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class))
                .thenReturn(recommendationReceivedEvent);
        when(userServiceClient.getUser(3L)).thenReturn(recipientUser);
        when(userServiceClient.getUser(2L)).thenReturn(authorUser);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(
                "You have received a new recommendation from authorUser.");

        recommendationReceivedEventListener.onMessage(message, null);

        verify(objectMapper).readValue(message.getBody(), RecommendationReceivedEvent.class);
        verify(userServiceClient, times(2)).getUser(anyLong());
        verify(messageSource).getMessage(any(), any(), any());
        verify(emailService).send(recipientUser, "You have received a new recommendation from authorUser.");
    }

    @Test
    @DisplayName("Success event processing with SMS sending")
    void recommendationReceivedEventListenerTest_successEventProcessingWithSmsSending() throws IOException {
        recipientUser.setPreference(UserDto.PreferredContact.SMS);

        when(objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class))
                .thenReturn(recommendationReceivedEvent);
        when(userServiceClient.getUser(3L)).thenReturn(recipientUser);
        when(userServiceClient.getUser(2L)).thenReturn(authorUser);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(
                "You have received a new recommendation from authorUser.");

        recommendationReceivedEventListener.onMessage(message, null);

        verify(objectMapper).readValue(message.getBody(), RecommendationReceivedEvent.class);
        verify(userServiceClient, times(2)).getUser(anyLong());
        verify(messageSource).getMessage(any(), any(), any());
        verify(smsService).send(recipientUser, "You have received a new recommendation from authorUser.");
    }

    @Test
    @DisplayName("Success event processing with Telegram sending")
    void recommendationReceivedEventListenerTest_successEventProcessingWithTelegramSending() throws IOException {
        recipientUser.setPreference(UserDto.PreferredContact.TELEGRAM);

        when(objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class))
                .thenReturn(recommendationReceivedEvent);
        when(userServiceClient.getUser(3L)).thenReturn(recipientUser);
        when(userServiceClient.getUser(2L)).thenReturn(authorUser);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(
                "You have received a new recommendation from authorUser.");

        recommendationReceivedEventListener.onMessage(message, null);

        verify(objectMapper).readValue(message.getBody(), RecommendationReceivedEvent.class);
        verify(userServiceClient, times(2)).getUser(anyLong());
        verify(messageSource).getMessage(any(), any(), any());
        verify(telegramService).send(recipientUser,
                "You have received a new recommendation from authorUser.");
    }

    @Test
    @DisplayName("ObjectMapper reading value error")
    void recommendationReceivedEventListenerTest_objectMapperReadingValueError() throws IOException {
        when(objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class))
                .thenThrow(new IOException("Failed to deserialize"));

        assertThrows(RuntimeException.class, () -> recommendationReceivedEventListener.onMessage(
                message, null));
        verify(objectMapper).readValue(message.getBody(), RecommendationReceivedEvent.class);
        verifyNoMoreInteractions(userServiceClient);
        verifyNoMoreInteractions(messageSource);
        verifyNoMoreInteractions(emailService, smsService, telegramService);
    }

    @Test
    @DisplayName("Getting message without MessageBuilder")
    void recommendationReceivedEventListenerTest_gettingMessageWithoutMessageBuilder() throws IOException {
        messageBuilders = new ArrayList<>();
        recommendationReceivedEventListener = new RecommendationReceivedEventListener(
                notificationServices, messageBuilders, userServiceClient, objectMapper);

        when(objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class))
                .thenReturn(recommendationReceivedEvent);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> recommendationReceivedEventListener.onMessage(message, null));

        assertEquals("No such message builder " +
                "found faang.school.notificationservice.dto.RecommendationReceivedEvent", ex.getMessage());
    }

    @Test
    @DisplayName("Getting message without NotificationService")
    void recommendationReceivedEventListenerTest_gettingMessageWithoutNotificationService() throws IOException {
        notificationServices = new ArrayList<>();
        recommendationReceivedEventListener = new RecommendationReceivedEventListener(
                notificationServices, messageBuilders, userServiceClient, objectMapper);

        when(objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class))
                .thenReturn(recommendationReceivedEvent);
        when(userServiceClient.getUser(3L)).thenReturn(recipientUser);
        when(userServiceClient.getUser(2L)).thenReturn(authorUser);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(
                "You have received a new recommendation from authorUser.");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> recommendationReceivedEventListener.onMessage(message, null));

        assertEquals("No such notification service found " + recipientUser.getPreference(), ex.getMessage());
    }
}