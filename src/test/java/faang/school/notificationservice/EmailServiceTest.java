package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.service.notification.EmailService;
import faang.school.notificationservice.validator.UserValidator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private EmailService emailService;

    private UserDto userDto;
    private SimpleMailMessage simpleMailMessage;
    private String message;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("user@example.com");

        message = "Test message";
        simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("test@example.com");
        simpleMailMessage.setTo(userDto.getEmail());
        simpleMailMessage.setText(message);

    }

    @Test
    void sendEmailWenInvalidUser() {
        userDto.setEmail(null);
        doThrow(DataValidationException.class).when(userValidator).checkEmail(userDto.getEmail());


        Assert.assertThrows(DataValidationException.class, () -> emailService.send(userDto, message));
    }

    @Test
    void sendSendEmailWhenValidUser() {
        doNothing().when(userValidator).checkEmail(userDto.getEmail());

        emailService.send(userDto, message);

        verify(userValidator).checkEmail(userDto.getEmail());
    }
}
