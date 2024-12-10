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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    public void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = emailService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.EMAIL, preferredContact);
    }

    @Test
    public void testSendEmail() {
        UserDto userDto = UserDto.builder()
                .email("some@mail.com")
                .build();

        emailService.send(userDto, "someText");
        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage message = messageCaptor.getValue();
        String actualEmail = Arrays.stream(message.getTo())
                .filter(email -> email.equals(userDto.getEmail()))
                .findFirst()
                .orElse(null);

        verify(javaMailSender, times(1)).send(message);
        assertEquals(userDto.getEmail(), actualEmail);
        assertEquals("someText", message.getText());
    }
}
