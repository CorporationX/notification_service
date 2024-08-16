package faang.school.notificationservice.service.email;

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
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    void getPreferredContactTest() {
        UserDto.PreferredContact preferredContact = emailService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.EMAIL, preferredContact);
    }

    @Test
    void sendTest() {
        String email = "user@gmail.com";
        String message = "Some test message";
        UserDto user = UserDto.builder()
                .id(1L)
                .email(email)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        emailService.send(user,message);
        verify(mailSender,times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(message,sentMessage.getText());
        assertEquals(email,sentMessage.getTo()[0]);
    }
}
