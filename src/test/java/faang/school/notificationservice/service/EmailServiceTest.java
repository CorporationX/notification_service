package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    ArgumentCaptor<SimpleMailMessage> captor;

    @Test
    public void sendMailTest() {
        UserDto userDto = UserDto.builder().build();
        userDto.setEmail("example@gmail.com");
        String message = "Hello world!";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userDto.getEmail());
        mailMessage.setText(message);

        emailService.send(userDto, message);

        verify(emailSender, times(1)).send(captor.capture());
        assertEquals(mailMessage, captor.getValue());
    }

    @Test
    public void getPreferredContactTest() {
        UserDto.PreferredContact preferredContact = emailService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.EMAIL, preferredContact);
    }
}
