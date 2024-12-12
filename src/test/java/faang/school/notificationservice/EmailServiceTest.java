package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender emailSender;
    @InjectMocks
    private EmailService emailService;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    private final String mailMessage = "Email message notification";

    @Test
    void sendMessageSuccessTest() {
        UserDto user = getUserDto();

        emailService.send(user, mailMessage);
        verify(emailSender).send(messageCaptor.capture());
        assertEquals(user.getEmail(), Objects.requireNonNull(messageCaptor.getValue().getTo())[0]);
        assertEquals(mailMessage, messageCaptor.getValue().getText());
    }

    @Test
    void sendMessageWithNullEmailFailTest() {
        UserDto user = getUserDto();
        user.setEmail(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> emailService.send(user, mailMessage));
        String expectedMessage = String.format(EmailService.UNABLE_TO_SEND_NOTIFICATION, user.getId());
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(emailSender, never()).send(messageCaptor.capture());
    }

    @Test
    void sendMessageWithIncorrectEmailFailTest() {
        UserDto user = getUserDto();
        user.setEmail("abc");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> emailService.send(user, mailMessage));
        String expectedMessage = String.format(EmailService.UNABLE_TO_SEND_NOTIFICATION, user.getId());
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(emailSender, never()).send(messageCaptor.capture());
    }

    @Test
    void sendMessageWithNotEmailPreferenceContactFailTest() {
        UserDto user = getUserDto();
        user.setPreference(UserDto.PreferredContact.SMS);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> emailService.send(user, mailMessage));
        String expectedMessage = String.format(EmailService.NOT_THE_PREFERRED_PLATFORM, user.getId());
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(emailSender, never()).send(messageCaptor.capture());
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .id(25)
                .username("User")
                .email("user_mail@mail.ru")
                .phone("+79991122334")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
}
