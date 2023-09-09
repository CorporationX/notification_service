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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;
    private UserDto user;
    private SimpleMailMessage message;
    private String text;

    @BeforeEach
    void setUp() {
        text = "text";
        String email = "email";
        String subject = "Corporation-X notification";
        message = new SimpleMailMessage();
        message.setTo(email);
        message.setText(text);
        message.setSubject(subject);
        user = UserDto.builder().email(email).build();
    }

    @Test
    void getPreferredContact_shouldReturnEmail() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }

    @Test
    void send_shouldInvokeMailSenderSend() {
        emailService.send(user, text);
        verify(mailSender).send(message);
    }
}