package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void getPreferredContactSuccess() {
        UserDto.PreferredContact result = emailService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.EMAIL, result);
    }

    @Test
    void sendSuccess(){
        String testText = "This text is for testing purposes";
        UserDto user = new UserDto();
        user.setEmail("xxx@yyy.com");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Goal has been achieved!");
        message.setText(testText);

        emailService.send(user, testText);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mailSender, Mockito.times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("xxx@yyy.com", sentMessage.getTo()[0]);
        assertEquals("Goal has been achieved!", sentMessage.getSubject());
        assertEquals(testText, sentMessage.getText());
    }
}