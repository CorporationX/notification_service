package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    private UserDto user;
    private SimpleMailMessage correct;

    @BeforeEach
    void setUp() {
        user = UserDto.builder()
                .id(1L)
                .username("username")
                .email("test@domain.com")
                .build();

        correct = new SimpleMailMessage();
        correct.setTo(user.getEmail());
        correct.setSubject("Hello, " + "username");
        correct.setText("Test Text");
    }

    @Test
    void send_Success() {
        emailService.send(user, "Test Text");

        verify(mailService).send(messageCaptor.capture());
        SimpleMailMessage result = messageCaptor.getValue();
        assertEquals(correct, result);
    }

    @Test
    void getPreferredContact_ReturnsEmail() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }
}
