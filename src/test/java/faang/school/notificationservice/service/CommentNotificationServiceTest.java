package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.exception.UserServiceException;
import faang.school.notificationservice.messaging.MessageBuilder;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentNotificationServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<CommentEvent> messageBuilder;

    @Mock
    private EmailService emailService;

    @Mock
    private PhoneNotificationService phoneService;

    @Mock
    private TelegramService telegramService;

    private CommentNotificationService commentNotificationService;

    private UserDto testUser;
    private CommentEvent testEvent;

    @BeforeEach
    void setUp() {
        Map<UserDto.PreferredContact, NotificationService> notificationServices = Map.of(
                UserDto.PreferredContact.EMAIL, emailService,
                UserDto.PreferredContact.PHONE, phoneService,
                UserDto.PreferredContact.TELEGRAM, telegramService
        );

        commentNotificationService = new CommentNotificationService(
                List.of(messageBuilder),
                userServiceClient,
                notificationServices
        );

        testUser = new UserDto();
        testUser.setId(4L);
        testUser.setUsername("testuser");
        testUser.setEmail("user@test.com");
        testUser.setPhone("+1234567890");

        testEvent = new CommentEvent(1L, 2L, 3L, 4L, "Test comment", LocalDateTime.now());
    }

    @ParameterizedTest
    @EnumSource(UserDto.PreferredContact.class)
    @DisplayName("Should send notification successfully for all PreferredContact values")
    void testSendNotificationSuccessfully(UserDto.PreferredContact contact) {
        testUser.setPreference(contact);

        when(userServiceClient.getUser(4L)).thenReturn(testUser);
        when(messageBuilder.getInstance()).thenReturn((Class) CommentEvent.class);
        when(messageBuilder.buildMessage(eq(testEvent), eq(Locale.ENGLISH))).thenReturn("Test message");

        assertDoesNotThrow(() -> commentNotificationService.sendCommentNotification(testEvent));

        switch (contact) {
            case EMAIL -> verify(emailService).send(testUser, "Test message");
            case PHONE -> verify(phoneService).send(testUser, "Test message");
            case TELEGRAM -> verify(telegramService).send(testUser, "Test message");
        }
    }

    @Test
    @DisplayName("Should throw UserNotFoundException if user not found")
    void testUserNotFound() {
        when(userServiceClient.getUser(4L)).thenThrow(FeignException.NotFound.class);

        assertThrows(UserNotFoundException.class,
                () -> commentNotificationService.sendCommentNotification(testEvent));

        verifyNoInteractions(emailService, phoneService, telegramService);
    }

    @Test
    @DisplayName("Should throw MessageBuilderNotFoundException if no builder available")
    void testMissingMessageBuilder() {
        testUser.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(4L)).thenReturn(testUser);
        when(messageBuilder.getInstance()).thenReturn((Class) Object.class);

        assertThrows(MessageBuilderNotFoundException.class,
                () -> commentNotificationService.sendCommentNotification(testEvent));

        verifyNoInteractions(emailService, phoneService, telegramService);
    }

    @Test
    @DisplayName("Should not send notification if preferred contact is null")
    void testUnsupportedPreferredContact() {
        testUser.setPreference(null);

        when(userServiceClient.getUser(4L)).thenReturn(testUser);
        when(messageBuilder.getInstance()).thenReturn((Class) CommentEvent.class);
        when(messageBuilder.buildMessage(any(), any())).thenReturn("Test message");

        assertDoesNotThrow(() -> commentNotificationService.sendCommentNotification(testEvent));

        verifyNoInteractions(emailService, phoneService, telegramService);
    }

    @Test
    @DisplayName("Should wrap delivery failures into UserServiceException")
    void testNotificationServiceThrowsException() {
        testUser.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(4L)).thenReturn(testUser);
        when(messageBuilder.getInstance()).thenReturn((Class) CommentEvent.class);
        when(messageBuilder.buildMessage(any(), any())).thenReturn("Test message");
        doThrow(new RuntimeException("Delivery failed"))
                .when(emailService).send(any(), any());

        assertThrows(UserServiceException.class,
                () -> commentNotificationService.sendCommentNotification(testEvent));

        verify(emailService).send(testUser, "Test message");
    }
}
