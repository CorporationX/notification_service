package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.service.email.EmailService;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private MentorshipOfferedMessageBuilder messageBuilder;

    @Mock
    private EmailService emailService;

    @Mock
    private TelegramService telegramService;

    @Spy
    @InjectMocks
    private EventService eventService;

    private static final long REQUEST_ID = 1L;
    private static final long AUTHOR_ID = 2L;
    private static final long RECEIVER_ID = 3L;
    private static final String TEST_MESSAGE = "Test mentorship offered message";

    @BeforeEach
    void setUp() {
        when(messageBuilder.buildMessage(any(MentorshipOfferedEvent.class), eq(Locale.ENGLISH)))
                .thenReturn(TEST_MESSAGE);
    }

    @Test
    void whenUserPreferenceIsEmail_thenSendEmailNotification() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        doReturn(userDto).when(eventService).getUserDto(any());

        // Act
        eventService.sendMentorshipOfferedMessage(REQUEST_ID, AUTHOR_ID, RECEIVER_ID);

        // Assert
        verify(emailService).send(eq(userDto), eq(TEST_MESSAGE));
        verify(telegramService, never()).send(any(), any());
    }

    @Test
    void whenUserPreferenceIsTelegram_thenSendTelegramNotification() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.TELEGRAM);
        doReturn(userDto).when(eventService).getUserDto(any());

        // Act
        eventService.sendMentorshipOfferedMessage(REQUEST_ID, AUTHOR_ID, RECEIVER_ID);

        // Assert
        verify(telegramService).send(eq(userDto), eq(TEST_MESSAGE));
        verify(emailService, never()).send(any(), any());
    }

    @Test
    void whenMessageBuilderCalled_thenCorrectParametersUsed() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        doReturn(userDto).when(eventService).getUserDto(any());

        // Act
        eventService.sendMentorshipOfferedMessage(REQUEST_ID, AUTHOR_ID, RECEIVER_ID);

        // Assert
        verify(messageBuilder).buildMessage(
                argThat(event ->
                        event.idRequest() == REQUEST_ID &&
                                event.idAuthor() == AUTHOR_ID  &&
                                event.idReceiver() == RECEIVER_ID
                ),
                eq(Locale.ENGLISH)
        );
    }

    @Test
    void whenGetUserDtoCalled_thenCorrectIdPassed() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        doReturn(userDto).when(eventService).getUserDto(REQUEST_ID);

        // Act
        eventService.sendMentorshipOfferedMessage(REQUEST_ID, AUTHOR_ID, RECEIVER_ID);

        // Assert
        verify(eventService).getUserDto(eq(REQUEST_ID));
    }

    @Test
    void whenUserPreferenceIsNull_thenThrowException() {

        UserDto userDto = new UserDto();
        userDto.setPreference(null);
        doReturn(userDto).when(eventService).getUserDto(any());

        assertThrows(NullPointerException.class,
                () -> eventService.sendMentorshipOfferedMessage(REQUEST_ID, AUTHOR_ID, RECEIVER_ID),
                "Should throw IllegalArgumentException for null preference"
        );
    }
}