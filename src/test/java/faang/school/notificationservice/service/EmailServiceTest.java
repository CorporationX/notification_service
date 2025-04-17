package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.properties.MailProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MailProperties mailProperties;

    private final String text = "text";
    private final String toEmail = "some@gmail.com";
    private final String fromEmail = "company@gmail.com";
    private final String[] toEmails = {toEmail};
    private final UserDto user = UserDto.builder().email(toEmail).build();
    private final UserDto.PreferredContact preferredContact = UserDto.PreferredContact.EMAIL;

    @Test
    void send_ShouldSend() {
        when(mailProperties.getUsername()).thenReturn(fromEmail);
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        assertDoesNotThrow(() -> emailService.send(user, text));
        verify(mailProperties, times(1)).getUsername();
        verify(emailSender, times(1)).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertNotNull(message.getTo());
        assertNotNull(message.getFrom());
        assertNotNull(message.getText());
        assertTrue(message.getTo().length == 1);
        assertEquals(toEmails[0], message.getTo()[0]);
        assertEquals(fromEmail, message.getFrom());
        assertEquals(text, message.getText());
    }

    @Test
    void getPreferredContact_ShouldGet() {
        UserDto.PreferredContact resultPreferred = emailService.getPreferredContact();
        assertEquals(preferredContact, resultPreferred);
    }

}