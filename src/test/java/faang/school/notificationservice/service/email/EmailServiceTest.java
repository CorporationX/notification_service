package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.validation.EmailValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;
    @Mock
    private EmailValidator emailValidator;

    @InjectMocks
    private EmailService emailService;

    private static final String MESSAGE = "MESSAGE";

    @Test
    @DisplayName("Should send message when input is valid")
    public void sendMessage() {
        UserDto userDto = createUserDto();
        SimpleMailMessage message = createMessage(userDto.getEmail(), userDto.getUsername());

        emailService.send(userDto, MESSAGE);

        verify(emailSender).send(message);
        verify(emailValidator).validateForEmail(userDto);
    }

    @Test
    @DisplayName("Should return preferred contact type as EMAIL")
    void returnEmailPreferredContact() {
        assertEquals(EMAIL, emailService.getPreferredContact());
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail("mail@mail.com");
        userDto.setUsername("Username");
        userDto.setPreference(EMAIL);

        return userDto;
    }

    private SimpleMailMessage createMessage(String to, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(MESSAGE);

        return message;
    }
}
