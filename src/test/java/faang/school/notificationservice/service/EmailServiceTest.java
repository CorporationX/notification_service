package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserContactsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSenderImpl javaMailSender;

    @InjectMocks
    private EmailService emailService;


    @Test
    void testEmailSending() {
        UserContactsDto user = UserContactsDto.builder()
                .email("doniyor.kurbanov.21@gmail.com")
                .build();

        String message = "Test Message";

        emailService.send(user, message);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
