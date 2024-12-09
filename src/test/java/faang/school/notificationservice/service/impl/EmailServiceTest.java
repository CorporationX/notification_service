package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserForNotificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void testSend() {
        // arrange
        String emailAddress = "someemail.gmail.com";
        UserForNotificationDto user = UserForNotificationDto.builder()
                .email(emailAddress)
                .build();
        String message = "someMessage";

        // act
        emailService.send(user, message);
        ArgumentCaptor<SimpleMailMessage> mailMessage
                = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // assert
        verify(javaMailSender).send(mailMessage.capture());
        SimpleMailMessage sentMessage = mailMessage.getValue();
        assertEquals(emailAddress, sentMessage.getTo()[0]);
        assertEquals(message, sentMessage.getText());
    }

    @Test
    public void testGetPreferredContact() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }
}
