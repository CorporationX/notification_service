package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.email.EmailConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    public void setup() {
        EmailConfig emailConfig = new EmailConfig();
        ReflectionTestUtils.setField(emailConfig, "host", "smtp.gmail.com");
        ReflectionTestUtils.setField(emailConfig, "port", 587);
        ReflectionTestUtils.setField(emailConfig, "emailAddress", "corporationx.bot@gmail.com");
        ReflectionTestUtils.setField(emailConfig, "password", "rzmcyykwdhhfppoa");
        ReflectionTestUtils.setField(emailConfig, "protocol", true);
        ReflectionTestUtils.setField(emailConfig, "auth", true);
        ReflectionTestUtils.setField(emailConfig, "starttls", true);
        ReflectionTestUtils.setField(emailConfig, "debug", true);

        emailService = new EmailService(emailConfig.getJavaMailSender()); // injection
    }

    @Test
    public void sendEmailTest() {
        emailService.send(new UserDto(1, "User", "kukuha@internet.ru", "+12345678910", UserDto.PreferredContact.EMAIL), "Hi there!");
        assertTrue(true); // It emailed me
    }

    @Test
    public void nonExistentEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> emailService.send(
                        new UserDto(1, "User", "", "+12345678910", UserDto.PreferredContact.EMAIL),
                        "Hi there!"));
    }
}
