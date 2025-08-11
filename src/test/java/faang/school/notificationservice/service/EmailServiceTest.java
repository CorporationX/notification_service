package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

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

    @InjectMocks
    private EmailService emailService;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserDto();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("+1234567890");
        testUser.setPreference(UserDto.PreferredContact.EMAIL);

        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@example.com");
    }

    @Test
    @DisplayName("Should successfully send email to user")
    void testSendEmailSuccess() {
        String message = "Test notification message";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.send(testUser, message);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(mailSender, times(1)).createMimeMessage();
    }

    @Test
    @DisplayName("Should throw exception when user is null")
    void testSendEmailWithNullUser() {
        String message = "Test message";

        assertThatThrownBy(() -> emailService.send(null, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User cannot be null");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should throw exception when user email is null")
    void testSendEmailWithNullEmail() {
        testUser.setEmail(null);
        String message = "Test message";

        assertThatThrownBy(() -> emailService.send(testUser, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email is not set for user");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should throw exception when user email is empty")
    void testSendEmailWithEmptyEmail() {
        testUser.setEmail("");
        String message = "Test message";

        assertThatThrownBy(() -> emailService.send(testUser, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email is not set for user");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "@example.com", "user@", "user@.com", "user space@test.com"})
    @DisplayName("Should throw exception when email format is invalid")
    void testSendEmailWithInvalidEmailFormat(String invalidEmail) {
        testUser.setEmail(invalidEmail);
        String message = "Test message";

        assertThatThrownBy(() -> emailService.send(testUser, message))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");

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

        assertDoesNotThrow(() -> emailService.send(testUser, "Test"));
    }

    @Test
    @DisplayName("Should handle mail sending exception")
    void testSendEmailMailException() {
        String message = "Test message";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("SMTP connection failed"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> emailService.send(testUser, message))
                .isInstanceOf(MailException.class)
                .hasMessageContaining("SMTP connection failed");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle message creation exception")
    void testSendEmailMessagingException() {
        String message = "Test message";
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Failed to create message"));

        assertThatThrownBy(() -> emailService.send(testUser, message))
                .isInstanceOf(RuntimeException.class);

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should return EMAIL as preferred contact")
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = emailService.getPreferredContact();

        assertThat(preferredContact).isEqualTo(UserDto.PreferredContact.EMAIL);
    }

    @Test
    @DisplayName("Should handle special characters in message")
    void testSpecialCharactersInMessage() {
        String message = "Test & message with <special> \"characters\" 'and' line\nbreaks";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.send(testUser, message);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void testNullMessage() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.send(testUser, null);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should handle user without username")
    void testUserWithoutUsername() {
        testUser.setUsername(null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.send(testUser, "Test message");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should create message with correct from address")
    void testFromAddress() {
        String message = "Test message";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.send(testUser, message);

        verify(mailSender).send(any(MimeMessage.class));

        assertThat(ReflectionTestUtils.getField(emailService, "fromEmail"))
                .isEqualTo("noreply@example.com");
    }
}
