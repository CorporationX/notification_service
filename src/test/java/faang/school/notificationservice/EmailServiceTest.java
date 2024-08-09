package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Named;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender emailMailSender;
    @InjectMocks
    private EmailService emailService;

    @Value("${spring.mail.subject}")
    private String subject;
    @Value("${spring.mail.username}")
    private String sender;

    private UserDto userDto;

    @BeforeEach
    void init() {
        userDto = UserDto.builder().
                email("test@test.com").
                build();
    }

    @Test
    @Named("Test send method")
    public void testSend() {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(sender);
        email.setSubject(subject);
        email.setTo(userDto.getEmail());
        email.setText("test");
        emailService.send(userDto, "test");

        verify(emailMailSender).send(email);
    }
}
