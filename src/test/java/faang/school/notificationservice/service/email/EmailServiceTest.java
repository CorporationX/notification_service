package faang.school.notificationservice.service.email;

import faang.school.notificationservice.model.dto.UserDto;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "spring.mail.username=test@gmail.com"  // your SMTP configuration
})
public class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender mailSender;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("dovgan.denys.ws@gmail.com")
                .username("denisKotoryiShlepa")
                .build();
    }

    @Test
    void testSendEmail_Success() {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        emailService.setEmailFrom("test@gmail.com");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.send(userDto, "Test msg");

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
