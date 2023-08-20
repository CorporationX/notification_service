package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

 import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;

    @Test
    void testSendMail() {
        UserDto user = UserDto.builder()
                .email("email@.com")
                .build();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
            message.setSubject("subject");
        message.setText("text");

        emailService.send(user, "text");

        verify(mailSender, times(1)).send(message);
    }
}
