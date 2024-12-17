package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.SubscriptionEvent;
import faang.school.notificationservice.messaging.SubscriptionMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserFeignService userFeignService;

    @Mock
    private SubscriptionMessageBuilder subscriptionMessageBuilder;

    @Mock
    private NotificationService smsNotificationService;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService telegramNotificationService;

    @Mock
    private Message message;

    @InjectMocks
    private SubscriptionEventListener subscriptionEventListener;

    private List<NotificationService> notificationServices;
    private Long userId;
    private UserContactsDto userContactsDto;
    private SubscriptionEvent subscriptionEvent;
    private String messageText;
    private Locale locale;
    private String validJson;
    private String invalidJson;

    @BeforeEach
    void setUp() {
        notificationServices = List.of(smsNotificationService, emailNotificationService, telegramNotificationService);
        subscriptionEventListener = new SubscriptionEventListener(objectMapper, userFeignService,
                subscriptionMessageBuilder, notificationServices);

        userId = 1L;
        userContactsDto = UserContactsDto.builder()
                .id(userId)
                .preference(NotificationChannel.EMAIL)
                .build();
        subscriptionEvent = SubscriptionEvent.builder()
                .followeeId(userId)
                .followerName("follower")
                .followeeName("followee")
                .build();
        messageText = "notification message!";
        locale = Locale.getDefault();
        validJson = "{\"followerId\":1,\"followeeId\":2}";
        invalidJson = "invalid_json";
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        when(message.getBody()).thenReturn(validJson.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(validJson.getBytes(StandardCharsets.UTF_8), SubscriptionEvent.class))
                .thenReturn(subscriptionEvent);
        when(userFeignService.getUserContacts(userId)).thenReturn(userContactsDto);
        when(subscriptionMessageBuilder.buildMessage(subscriptionEvent, locale)).thenReturn(messageText);
        when(emailNotificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);
        doNothing().when(emailNotificationService).send(userContactsDto, messageText);

        subscriptionEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(validJson.getBytes(StandardCharsets.UTF_8), SubscriptionEvent.class);
        verify(userFeignService, times(1)).getUserContacts(userId);
        verify(subscriptionMessageBuilder, times(1)).buildMessage(subscriptionEvent, locale);
        verify(emailNotificationService, times(1)).send(userContactsDto, messageText);
    }

    @Test
    void testOnMessageDeserializationError() throws IOException {
        when(message.getBody()).thenReturn(invalidJson.getBytes(StandardCharsets.UTF_8));
        doThrow(new IOException("Deserialization failed"))
                .when(objectMapper).readValue(any(byte[].class), eq(SubscriptionEvent.class));

        subscriptionEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(invalidJson.getBytes(StandardCharsets.UTF_8), SubscriptionEvent.class);
        verifyNoInteractions(userFeignService, subscriptionMessageBuilder, emailNotificationService,
                smsNotificationService, telegramNotificationService);
    }

    @Test
    void testOnMessageNoPreferredNotificationServices() throws IOException {
        when(message.getBody()).thenReturn(validJson.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(validJson.getBytes(StandardCharsets.UTF_8), SubscriptionEvent.class))
                .thenReturn(subscriptionEvent);
        when(userFeignService.getUserContacts(userId)).thenReturn(userContactsDto);
        when(subscriptionMessageBuilder.buildMessage(subscriptionEvent, locale)).thenReturn(messageText);
        when(emailNotificationService.getPreferredContact()).thenReturn(null);

        subscriptionEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(validJson.getBytes(StandardCharsets.UTF_8), SubscriptionEvent.class);
        verify(userFeignService, times(1)).getUserContacts(userId);
        verify(subscriptionMessageBuilder, times(1)).buildMessage(subscriptionEvent, locale);
        verify(emailNotificationService, times(0)).send(any(), any());
        verify(smsNotificationService, times(0)).send(any(), any());
        verify(telegramNotificationService, times(0)).send(any(), any());
    }
}