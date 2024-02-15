package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailService emailService;

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = UserDto.PreferredContact.EMAIL;
        assertEquals(preferredContact, emailService.getPreferredContact());
    }

    @Test
    void testSendCallsMethodJavaMailSender() {
        UserDto user = UserDto.builder().build();
        String text = "text";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("atraxlxxxl@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Notification from CorporationX");
        message.setText(text);
        emailService.send(user, text);
        verify(javaMailSender, times(1)).send(message);
    }
}