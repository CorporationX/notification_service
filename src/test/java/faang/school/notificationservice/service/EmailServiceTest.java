package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final EmailService emailService = new EmailService(mailSender);

    @Test
    void test_ShouldSendEmail() {
        UserDto user = new UserDto();
        user.setEmail("test@gmail.com");

        emailService.send(user, "Test message");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();
        assertEquals("test@gmail.com", sent.getTo()[0]);
        assertEquals("Notification", sent.getSubject());
        assertEquals("Test message", sent.getText());
    }

    @Test
    void test_ShouldThrowIfEmailMissing() {
        UserDto user = new UserDto();

        assertThrows(IllegalArgumentException.class, () -> emailService.send(user, "test"));
    }

    @Test
    void test_ShouldReturnEmailAsPreferredContact() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }
}