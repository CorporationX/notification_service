package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    EmailService emailService;

    @Test
    void TestSend() {
        //Arrange
        UserDto user = new UserDto();
        String text = "Text message";
        //Act
        emailService.send(user, text);
        //Assert
        verify(mailSender, times(1)).send((SimpleMailMessage) any());
    }

    @Test
    void testGetPreferredContact() {
        //Act
        PreferredContact result = emailService.getPreferredContact();
        //Assert
        assertEquals(result, PreferredContact.EMAIL);
    }
}