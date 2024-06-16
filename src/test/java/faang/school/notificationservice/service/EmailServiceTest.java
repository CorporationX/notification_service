package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private EmailService emailService;
    @Value("${spring.mail.sender.email}")
    private String senderMail;
    private UserNotificationDto userNotificationDto;

    @BeforeEach
    void setUp() {
        userNotificationDto = UserNotificationDto.builder()
                .email("test@mail.com")
                .preference(UserNotificationDto.PreferredContact.EMAIL)
                .build();
    }

    @Test
    void whenSendSuccessfully() {
        String text = "text";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(userNotificationDto.getEmail());
        message.setText(text);
        emailService.send(userNotificationDto, text);
        verify(mailSender).send(message);
    }
}