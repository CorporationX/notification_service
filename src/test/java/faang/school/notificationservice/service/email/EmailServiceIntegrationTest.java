package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")  // Ensure that you're using a test-specific configuration
@TestPropertySource(properties = {
        "spring.mail.host=smtp.gmail.com",  // your SMTP configuration
        "spring.mail.port=587",
        "spring.mail.username=senderemail@test.com",  // update with your actual username
        "spring.mail.password=apppassword",  // update with your actual password or app-specific password
        "spring.mail.properties.mail.smtp.auth=true",
        "spring.mail.properties.mail.smtp.starttls.enable=true"
})
public class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @SpyBean
    private JavaMailSender mailSender;

    @Test
    public void testSendEmail_Success() throws MessagingException {
        // Arrange
        UserDto user = UserDto.builder()
                .username("TestUser")
                .email("dovgan.denys.ws@gmail.com")
                .build();
        String message = "This is a test email.";

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.send(user, message);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        assertThat(mimeMessage).isNotNull();
    }
}
