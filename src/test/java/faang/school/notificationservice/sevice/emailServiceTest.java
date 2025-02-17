package faang.school.notificationservice.sevice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
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
public class emailServiceTest {
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private SimpleMailMessage templateMessage;
    @InjectMocks
    private EmailService emailService;

    private UserDto user;
    private final String MESSAGE = "test message";

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setUsername("test name");
        user.setEmail("example@example.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);
    }


    @Test
    public void testSendEmailSuccess() {
        emailService.send(user, MESSAGE);
        verify(emailSender, times(1)).send(templateMessage);
    }

}
