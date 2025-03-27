package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private MailSender mailSender;
    @InjectMocks
    private EmailService emailService;
    private SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    private UserNotificationDto userDto;
    private String message = "test message";

    @BeforeEach
    void setup() {
        userDto = new UserNotificationDto();
        userDto.setEmail("test@mail.com");
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject("Like is set to your post");
        simpleMailMessage.setTo(userDto.getEmail());
    }
    @Test
    public void testSendEmail() {
        emailService.send(userDto, message);
        Mockito.verify(mailSender).send(simpleMailMessage);
    }
}
