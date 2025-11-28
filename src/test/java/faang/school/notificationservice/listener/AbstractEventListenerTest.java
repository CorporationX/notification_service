package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.listeners.AbstractEventListener;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService smsNotificationService;

    @Mock
    private MessageBuilder<MentorshipOfferedEvent> mentorshipMessageBuilder;

    private TestEventListener testEventListener;

    private UserDto testUser;
    private MentorshipOfferedEvent testEvent;

    @BeforeEach
    void setUp() {
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(smsNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);
        when(mentorshipMessageBuilder.getInstance()).thenReturn((Class) MentorshipOfferedEvent.class);

        List<NotificationService> notificationServices = List.of(emailNotificationService, smsNotificationService);
        List<MessageBuilder<?>> messageBuilders = List.of(mentorshipMessageBuilder);

        testEventListener = new TestEventListener(
                objectMapper,
                userService,
                notificationServices,
                messageBuilders
        );

        testUser = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .phone("+1234567890")
                .locale(Locale.ENGLISH)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        testEvent = MentorshipOfferedEvent.builder()
                .mentorshipRequestId(1L)
                .mentorId(1L)
                .menteeId(2L)
                .build();
    }

    @Test
    void getMessage_shouldReturnBuiltMessage_whenMessageBuilderExists() {
        String expectedMessage = "You've a new mentorship request";
        when(mentorshipMessageBuilder.buildMessage(eq(testEvent), eq(Locale.ENGLISH)))
                .thenReturn(expectedMessage);

        String actualMessage = testEventListener.getMessage(testEvent, MentorshipOfferedEvent.class, Locale.ENGLISH);

        assertEquals(expectedMessage, actualMessage);
        verify(mentorshipMessageBuilder).buildMessage(testEvent, Locale.ENGLISH);
    }

    @Test
    void getMessage_shouldThrowException_whenMessageBuilderNotFound() {
        Class<?> unknownEventClass = String.class;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> testEventListener.getMessage("unknown", unknownEventClass, Locale.ENGLISH)
        );

        assertTrue(exception.getMessage().contains("No message builder available"));
        assertTrue(exception.getMessage().contains(unknownEventClass.getName()));
    }

    @Test
    void sendNotification_shouldSendViaEmailService_whenUserPrefersEmail() {
        String message = "Test notification";
        when(userService.getUser(1L)).thenReturn(testUser);

        testEventListener.sendNotification(1L, message);

        verify(userService).getUser(1L);
        verify(emailNotificationService).send(testUser, message);
        verify(smsNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotification_shouldSendViaSmsService_whenUserPrefersSms() {
        String message = "Test notification";
        testUser.setPreference(UserDto.PreferredContact.SMS);
        when(userService.getUser(1L)).thenReturn(testUser);

        testEventListener.sendNotification(1L, message);

        verify(userService).getUser(1L);
        verify(smsNotificationService).send(testUser, message);
        verify(emailNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotification_shouldThrowException_whenNotificationServiceNotFound() {
        String message = "Test notification";
        testUser.setPreference(UserDto.PreferredContact.TELEGRAM);
        when(userService.getUser(1L)).thenReturn(testUser);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> testEventListener.sendNotification(1L, message)
        );

        assertTrue(exception.getMessage().contains("No notification service available"));
        assertTrue(exception.getMessage().contains("TELEGRAM"));
        verify(userService).getUser(1L);
        verify(emailNotificationService, never()).send(any(), any());
        verify(smsNotificationService, never()).send(any(), any());
    }

    @Test
    void constructor_shouldInitializeMaps_withProvidedServices() {
        assertNotNull(testEventListener.notificationServiceMap);
        assertNotNull(testEventListener.messageBuilderMap);
        assertEquals(2, testEventListener.notificationServiceMap.size());
        assertEquals(1, testEventListener.messageBuilderMap.size());
        assertTrue(testEventListener.notificationServiceMap.containsKey(UserDto.PreferredContact.EMAIL));
        assertTrue(testEventListener.notificationServiceMap.containsKey(UserDto.PreferredContact.SMS));
        assertTrue(testEventListener.messageBuilderMap.containsKey(MentorshipOfferedEvent.class));
    }

    @Test
    void getMessage_shouldWorkWithDifferentLocales() {
        Locale frenchLocale = Locale.FRENCH;
        String expectedFrenchMessage = "Vous avez une nouvelle demande de mentorat";
        when(mentorshipMessageBuilder.buildMessage(eq(testEvent), eq(frenchLocale)))
                .thenReturn(expectedFrenchMessage);

        String actualMessage = testEventListener.getMessage(testEvent, MentorshipOfferedEvent.class, frenchLocale);

        assertEquals(expectedFrenchMessage, actualMessage);
        verify(mentorshipMessageBuilder).buildMessage(testEvent, frenchLocale);
    }

    @Test
    void sendNotification_shouldRetrieveUserOnce() {
        String message = "Test notification";
        when(userService.getUser(1L)).thenReturn(testUser);

        testEventListener.sendNotification(1L, message);

        verify(userService, times(1)).getUser(1L);
    }

    static class TestEventListener extends AbstractEventListener {
        public TestEventListener(
                ObjectMapper objectMapper,
                UserService userService,
                List<NotificationService> notificationServices,
                List<MessageBuilder<?>> messageBuilders) {
            super(objectMapper, userService, notificationServices, messageBuilders);
        }

        @Override
        public <T> String getMessage(T event, Class<?> eventClass, Locale locale) {
            return super.getMessage(event, eventClass, locale);
        }

        @Override
        public void sendNotification(long userId, String message) {
            super.sendNotification(userId, message);
        }
    }
}