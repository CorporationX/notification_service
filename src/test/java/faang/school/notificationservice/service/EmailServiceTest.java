package faang.school.notificationservice.service;

import faang.school.notificationservice.config.email.EmailProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmailMessageCreationException;
import faang.school.notificationservice.exception.EmailSendingException;
import faang.school.notificationservice.exception.InvalidEmailFormatException;
import faang.school.notificationservice.exception.InvalidUserException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private EmailProperties emailProperties;

    private EmailService emailService;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, Collections.emptyList(), emailProperties);

        testUser = new UserDto();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("+1234567890");
        testUser.setPreference(UserDto.PreferredContact.EMAIL);
    }

    @Test
    @DisplayName("Should successfully send email to user")
    void testSendEmailSuccess() {
        String message = "Test notification message";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");

        assertDoesNotThrow(() -> emailService.send(testUser, message));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(mailSender, times(1)).createMimeMessage();
    }

    @Test
    @DisplayName("Should throw InvalidUserException when user is null")
    void testSendEmailWithNullUser() {
        assertThatThrownBy(() -> emailService.send(null, "msg"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("User cannot be null");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should throw InvalidUserException when user email is null")
    void testSendEmailWithNullEmail() {
        testUser.setEmail(null);

        assertThatThrownBy(() -> emailService.send(testUser, "msg"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessageContaining("Email is not set for user");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should throw InvalidUserException when user email is empty")
    void testSendEmailWithEmptyEmail() {
        testUser.setEmail("");

        assertThatThrownBy(() -> emailService.send(testUser, "msg"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessageContaining("Email is not set for user");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "@example.com", "user@", "user@.com", "user space@test.com"})
    @DisplayName("Should throw InvalidEmailFormatException when email format is invalid")
    void testSendEmailWithInvalidEmailFormat(String invalidEmail) {
        testUser.setEmail(invalidEmail);

        assertThatThrownBy(() -> emailService.send(testUser, "msg"))
                .isInstanceOf(InvalidEmailFormatException.class)
                .hasMessageContaining("Invalid email format for user");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user@example.com",
            "user.name@example.com",
            "user+tag@example.co.uk",
            "user123@test-domain.org"
    })
    @DisplayName("Should accept valid email formats")
    void testValidEmailFormats(String validEmail) {
        testUser.setEmail(validEmail);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");

        assertDoesNotThrow(() -> emailService.send(testUser, "Test message"));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should throw EmailSendingException when MailException occurs during sending")
    void testSendEmailMailException() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");
        doThrow(new MailSendException("SMTP connection failed"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> emailService.send(testUser, "msg"))
                .isInstanceOf(EmailSendingException.class)
                .hasMessage("Failed to send email notification");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should throw EmailMessageCreationException when MessagingException occurs")
    void testSendEmailMessagingException() throws Exception {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");
        doThrow(new MessagingException("Failed to set header"))
                .when(mimeMessage).setHeader(any(), any());

        assertThatThrownBy(() -> emailService.send(testUser, "msg"))
                .isInstanceOf(EmailMessageCreationException.class)
                .hasMessage("Failed to create email message");
    }

    @Test
    @DisplayName("Should return EMAIL as preferred contact")
    void testGetPreferredContact() {
        assertThat(emailService.getPreferredContact())
                .isEqualTo(UserDto.PreferredContact.EMAIL);
    }

    @Test
    @DisplayName("Should handle special characters in message")
    void testSpecialCharactersInMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");

        String messageWithSpecialChars = "Test & msg <with> \"chars\" 'and'\nline breaks";

        assertDoesNotThrow(() -> emailService.send(testUser, messageWithSpecialChars));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void testNullMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");

        assertDoesNotThrow(() -> emailService.send(testUser, null));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle user without username")
    void testUserWithoutUsername() {
        testUser.setUsername(null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");

        assertDoesNotThrow(() -> emailService.send(testUser, "Test message"));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle user with empty username")
    void testUserWithEmptyUsername() {
        testUser.setUsername("");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");

        assertDoesNotThrow(() -> emailService.send(testUser, "Test message"));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle empty message gracefully")
    void testEmptyMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");

        assertDoesNotThrow(() -> emailService.send(testUser, ""));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle whitespace-only message")
    void testWhitespaceOnlyMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");

        assertDoesNotThrow(() -> emailService.send(testUser, "   \n\t   "));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle long message")
    void testLongMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailProperties.getFrom()).thenReturn("noreply@test.com");
        when(emailProperties.getFromName()).thenReturn("Test Service");
        String longMessage = "Test message. ".repeat(100);

        assertDoesNotThrow(() -> emailService.send(testUser, longMessage));

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}