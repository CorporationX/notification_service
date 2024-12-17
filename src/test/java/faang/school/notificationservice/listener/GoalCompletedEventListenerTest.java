package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.List;

import static faang.school.notificationservice.data.NotificationChannel.EMAIL;
import static faang.school.notificationservice.data.NotificationChannel.SMS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {
    private static final String EXPECTED_MESSAGE = "Congrats! Goal with id: {0} completed by user with id: {1}";

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<GoalCompletedEvent> messageBuilder;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService smsNotificationService;

    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    private GoalCompletedEvent event;
    private UserContactsDto userContactsDto;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        event = new GoalCompletedEvent();
        event.setActorId(1L);

        userContactsDto = UserContactsDto.builder()
                .id(1L)
                .preference(EMAIL)
                .build();
    }

    @Test
    @DisplayName("Should send email notification")
    void shouldSendEmailNotification() {
        when(userServiceClient.getUserContacts(event.getActorId())).thenReturn(userContactsDto);
        when(messageBuilder.buildMessage(event, LocaleContextHolder.getLocale()))
                .thenReturn(EXPECTED_MESSAGE);

        when(emailNotificationService.getPreferredContact()).thenReturn(EMAIL);

        List<NotificationService> notificationServices = Arrays.asList(emailNotificationService, smsNotificationService);
        GoalCompletedEventListener goalCompletedEventListener = new GoalCompletedEventListener(objectMapper, userServiceClient, messageBuilder, notificationServices);

        goalCompletedEventListener.handleEvent(event);

        ArgumentCaptor<UserContactsDto> userCaptor = ArgumentCaptor.forClass(UserContactsDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailNotificationService, times(1)).send(userCaptor.capture(), messageCaptor.capture());

        assertEquals(userContactsDto.getId(), userCaptor.getValue().getId());
        assertEquals(EXPECTED_MESSAGE, messageCaptor.getValue());
    }

    @Test
    @DisplayName("Should not send notification if no service matches")
    void shouldNotSendNotificationIfNoServiceMatches() {
        when(userServiceClient.getUserContacts(event.getActorId())).thenReturn(userContactsDto);
        when(messageBuilder.buildMessage(event, LocaleContextHolder.getLocale()))
                .thenReturn(EXPECTED_MESSAGE);

        when(emailNotificationService.getPreferredContact()).thenReturn(SMS);
        when(smsNotificationService.getPreferredContact()).thenReturn(EMAIL);

        List<NotificationService> notificationServices = Arrays.asList(emailNotificationService, smsNotificationService);
        GoalCompletedEventListener goalCompletedEventListener = new GoalCompletedEventListener(objectMapper, userServiceClient, messageBuilder, notificationServices);

        goalCompletedEventListener.handleEvent(event);

        verify(emailNotificationService, never()).send(any(), any());
        verify(smsNotificationService, times(1)).send(any(), any());
    }

    @Test
    @DisplayName("Should handle FeignException when fetching user contacts")
    void shouldHandleFeignExceptionWhenFetchingUserContacts() {
        when(userServiceClient.getUserContacts(event.getActorId())).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> goalCompletedEventListener.handleEvent(event));
    }
}
