package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
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

    private UserDto userDto;
    private String message = "test message";
    private String subject = "Like is set to your post";

    @BeforeEach
    void setup() {
        userDto = new UserDto();
        userDto.setEmail("test@mail.com");
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setTo(userDto.getEmail());
    }
    @Test
    public void testSendEmail() {
        emailService.send(userDto.getEmail(), subject, message);
        Mockito.verify(mailSender).send(simpleMailMessage);
    }
}
