package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    private UserDto user;
    private String message;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setEmail("test@example.com");
        message = "Test message";
    }

    @Test
    void sendShouldUseEmailSender() {
        emailService.send(user, message);

        verify(emailSender).send(any(SimpleMailMessage.class));
    }
}
