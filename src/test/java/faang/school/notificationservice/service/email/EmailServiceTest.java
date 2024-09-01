package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    // Positive Tests

    @Test
    public void sendEmail_successfulTest() {
        String text = "This is a test";
        SimpleMailMessage message = new SimpleMailMessage();

        UserDto userDto = UserDto.builder().build();
        userDto.setEmail("email@email.com");

        message.setTo(userDto.getEmail());
        message.setSubject("subject");
        message.setText(text);

        emailService.send(userDto, text);

        verify(mailSender).send(message);
    }

    @Test
    public void sendEmail_throwUserTest() {
        String text = "This is a test";

        Assertions.assertThrows(IllegalArgumentException.class, () -> emailService.send(null, text));
    }

    @Test
    public void sendEmail_throwEmailTest() {
        String text = "This is a test";
        UserDto userDto = UserDto.builder().build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> emailService.send(userDto, text));
    }

    @Test
    public void sendEmail_throwTextTest() {
        UserDto userDto = UserDto.builder().build();
        userDto.setEmail("email@email.com");

        Assertions.assertThrows(IllegalArgumentException.class, () -> emailService.send(userDto, null));
    }
}
