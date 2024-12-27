package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    @Mock
    private UserFeignService userFeignService;

    @Mock
    private MessageBuilder<GoalCompletedEvent> messageBuilder;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService smsNotificationService;

    @Mock
    private GoalCompletedEvent event;

    @Mock
    private UserContactsDto userContactsDto;

    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;

    @BeforeEach
    void setUp() {
        goalCompletedEventListener = new GoalCompletedEventListener(
                mock(ObjectMapper.class),
                Arrays.asList(),
                Arrays.asList(emailNotificationService, smsNotificationService),
                messageBuilder,
                userFeignService
        );
    }

    @Test
    @DisplayName("Should send email notification when user has email")
    void shouldSendEmailNotification_whenUserHasEmail() {
        when(userFeignService.getUserContacts(event.getActorId())).thenReturn(userContactsDto);
        when(userContactsDto.getEmail()).thenReturn("test@example.com");
        when(userContactsDto.getPreference()).thenReturn(NotificationChannel.EMAIL);
        when(messageBuilder.buildMessage(event, LocaleContextHolder.getLocale())).thenReturn("Congrats! Goal completed!");

        when(emailNotificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);

        goalCompletedEventListener.handleEvent(event);

        ArgumentCaptor<UserContactsDto> userContactsCaptor = ArgumentCaptor.forClass(UserContactsDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailNotificationService, times(1))
                .send(userContactsCaptor.capture(), messageCaptor.capture());

        assertEquals("test@example.com", userContactsCaptor.getValue().getEmail());
        assertEquals("Congrats! Goal completed!", messageCaptor.getValue());
    }

    @Test
    @DisplayName("Should not send any notification when user has no contact info")
    void shouldNotSendNotification_whenUserHasNoContactInfo() {
        when(userFeignService.getUserContacts(event.getActorId())).thenReturn(userContactsDto);
        when(userContactsDto.getPreference()).thenReturn(null);
        when(messageBuilder.buildMessage(event, LocaleContextHolder.getLocale())).thenReturn("Congrats! Goal completed!");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            goalCompletedEventListener.handleEvent(event);
        });

        assertEquals("User 0 has no preference set", exception.getMessage());
    }

    @Test
    @DisplayName("Should send SMS notification when user has phone only")
    void shouldSendSmsNotification_whenUserHasPhoneOnly() {
        when(userFeignService.getUserContacts(event.getActorId())).thenReturn(userContactsDto);
        when(userContactsDto.getPhone()).thenReturn("1234567890");
        when(userContactsDto.getPreference()).thenReturn(NotificationChannel.SMS);
        when(messageBuilder.buildMessage(event, LocaleContextHolder.getLocale())).thenReturn("Congrats! Goal completed!");

        when(smsNotificationService.getPreferredContact()).thenReturn(NotificationChannel.SMS);

        goalCompletedEventListener.handleEvent(event);

        ArgumentCaptor<UserContactsDto> userContactsCaptor = ArgumentCaptor.forClass(UserContactsDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(smsNotificationService, times(1))
                .send(userContactsCaptor.capture(), messageCaptor.capture());

        assertEquals("1234567890", userContactsCaptor.getValue().getPhone());
        assertEquals("Congrats! Goal completed!", messageCaptor.getValue());
    }

    @Test
    @DisplayName("Should throw exception when user has no preference set")
    void shouldThrowException_whenUserHasNoPreference() {
        when(userFeignService.getUserContacts(event.getActorId())).thenReturn(userContactsDto);
        when(userContactsDto.getPreference()).thenReturn(null);
        when(messageBuilder.buildMessage(event, LocaleContextHolder.getLocale())).thenReturn("Congrats! Goal completed!");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> goalCompletedEventListener.handleEvent(event)
        );

        assertEquals("User 0 has no preference set", exception.getMessage());

        verify(emailNotificationService, never()).send(any(), any());
        verify(smsNotificationService, never()).send(any(), any());
    }
}
