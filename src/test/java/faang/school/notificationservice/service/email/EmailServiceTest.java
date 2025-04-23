package faang.school.notificationservice.service.email;


import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.handler.EmailSendingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(javaMailSender);
        setField(emailService, "emailSender", "no-reply@example.com");
        setField(emailService, "emailSubject", "Test Subject");
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = EmailService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSendValidEmailWhenSendSuccessfully() {
        UserDto user = new UserDto();
        user.setEmail("valid@example.com");

        CompletableFuture<Void> result = emailService.send(user, "Hello!");

        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendNullEmailWhenThrowEmailSendingException() {
        UserDto user = new UserDto();
        user.setEmail(null);

        CompletableFuture<Void> result = emailService.send(user, "Hello!");

        assertTrue(result.isCompletedExceptionally());
        result.exceptionally(ex -> {
            assertTrue(ex instanceof EmailSendingException);
            assertEquals("Email address cannot be null", ex.getMessage());
            return null;
        });

        verify(javaMailSender, never()).send((SimpleMailMessage) any());
    }

    @Test
    void testSendInvalidEmailFormatWhenThrowEmailSendingException() {
        UserDto user = new UserDto();
        user.setEmail("invalid-email");

        CompletableFuture<Void> result = emailService.send(user, "Hello!");

        assertTrue(result.isCompletedExceptionally());
        result.exceptionally(ex -> {
            assertTrue(ex instanceof EmailSendingException);
            assertTrue(ex.getMessage().contains("Invalid email address"));
            return null;
        });

        verify(javaMailSender, never()).send((SimpleMailMessage) any());
    }

    @Test
    void testSendMailSenderThrowsExceptionWhenWrapInEmailSendingException() {
        UserDto user = new UserDto();
        user.setEmail("valid@example.com");

        doThrow(new RuntimeException("SMTP failure")).when(javaMailSender).send(any(SimpleMailMessage.class));

        CompletableFuture<Void> result = emailService.send(user, "Hello!");

        assertTrue(result.isCompletedExceptionally());
        result.exceptionally(ex -> {
            assertTrue(ex instanceof EmailSendingException);
            assertTrue(ex.getMessage().contains("Unexpected error"));
            return null;
        });

        verify(javaMailSender, times(1)).send((SimpleMailMessage) any());
    }
}