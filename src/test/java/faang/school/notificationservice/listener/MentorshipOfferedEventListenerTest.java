package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.listeners.MentorshipOfferedEventListener;
import faang.school.notificationservice.messaging.message_builder.MentorshipOfferedEventMessageBuilder;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipOfferedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService smsNotificationService;

    @Mock
    private MentorshipOfferedEventMessageBuilder mentorshipMessageBuilder;

    private MentorshipOfferedEventListener listener;

    private UserDto mentor;
    private MentorshipOfferedEvent event;

    @BeforeEach
    void setUp() {
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(smsNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);
        when(mentorshipMessageBuilder.getInstance()).thenReturn((Class) MentorshipOfferedEvent.class);

        List<NotificationService> notificationServices = List.of(emailNotificationService, smsNotificationService);
        List<MessageBuilder<?>> messageBuilders = List.of(mentorshipMessageBuilder);

        listener = new MentorshipOfferedEventListener(
                objectMapper,
                userService,
                notificationServices,
                messageBuilders
        );

        mentor = UserDto.builder()
                .id(10L)
                .username("mentor")
                .email("mentor@example.com")
                .phone("+1234567890")
                .locale(Locale.ENGLISH)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        event = MentorshipOfferedEvent.builder()
                .mentorshipRequestId(100L)
                .mentorId(10L)
                .menteeId(20L)
                .build();
    }

    @Test
    void onMessage_shouldProcessEventAndSendNotification() {
        // Arrange
        String expectedMessage = "You've a new mentorship request";
        when(userService.getUser(10L)).thenReturn(mentor);
        when(mentorshipMessageBuilder.buildMessage(eq(event), eq(Locale.ENGLISH)))
                .thenReturn(expectedMessage);

        // Act
        listener.onMessage(event);

        // Assert
        verify(userService, times(2)).getUser(10L); // Once for getting locale, once for sending notification
        verify(mentorshipMessageBuilder).buildMessage(event, Locale.ENGLISH);
        verify(emailNotificationService).send(mentor, expectedMessage);
    }

    @Test
    void onMessage_shouldUseMentorLocale() {
        // Arrange
        mentor.setLocale(Locale.FRENCH);
        String expectedMessage = "Vous avez une nouvelle demande de mentorat";
        when(userService.getUser(10L)).thenReturn(mentor);
        when(mentorshipMessageBuilder.buildMessage(eq(event), eq(Locale.FRENCH)))
                .thenReturn(expectedMessage);

        // Act
        listener.onMessage(event);

        // Assert
        verify(mentorshipMessageBuilder).buildMessage(event, Locale.FRENCH);
        verify(emailNotificationService).send(mentor, expectedMessage);
    }

    @Test
    void onMessage_shouldUseSmsService_whenMentorPrefersSms() {
        // Arrange
        mentor.setPreference(UserDto.PreferredContact.SMS);
        String expectedMessage = "You've a new mentorship request";
        when(userService.getUser(10L)).thenReturn(mentor);
        when(mentorshipMessageBuilder.buildMessage(any(), any()))
                .thenReturn(expectedMessage);

        // Act
        listener.onMessage(event);

        // Assert
        verify(smsNotificationService).send(mentor, expectedMessage);
        verify(emailNotificationService, never()).send(any(), any());
    }

    @Test
    void onMessage_shouldSendNotificationToCorrectMentor() {
        // Arrange
        String expectedMessage = "You've a new mentorship request";
        when(userService.getUser(10L)).thenReturn(mentor);
        when(mentorshipMessageBuilder.buildMessage(any(), any()))
                .thenReturn(expectedMessage);

        // Act
        listener.onMessage(event);

        // Assert
        verify(userService, times(2)).getUser(event.mentorId());
        verify(emailNotificationService).send(mentor, expectedMessage);
    }

    @Test
    void onMessage_shouldNotSendToMentee() {
        // Arrange
        String expectedMessage = "You've a new mentorship request";
        when(userService.getUser(10L)).thenReturn(mentor);
        when(mentorshipMessageBuilder.buildMessage(any(), any()))
                .thenReturn(expectedMessage);

        // Act
        listener.onMessage(event);

        // Assert
        verify(userService, never()).getUser(event.menteeId());
    }
}