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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailService;

    private UserDto user;
    private SimpleMailMessage message;

    @BeforeEach
    void setUp() {
        user = UserDto.builder()
                .id(1L)
                .email("test@domain.com")
                .build();

        message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Test Subject");
        message.setText("Test Text");
    }

    @Test
    void send_Success() {
        emailService.send(user, message);

        verify(mailService).send(message);
    }

    @Test
    void getPreferredContact_ReturnsEmail() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }
}
