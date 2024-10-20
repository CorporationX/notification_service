package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailNotificationService;
import faang.school.notificationservice.service.email.EmailNotificationServiceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    private final JavaMailSender javaMailSender = mock(JavaMailSender.class);

    @InjectMocks
    private EmailNotificationService emailService;

    EmailNotificationServiceHandler emailServiceHandler = mock(EmailNotificationServiceHandler.class);

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    private UserDto userDto;
    private String message;

    @BeforeEach
    public void setUp() {
        String userEmail = "testUser@gmail.com";
        userDto = new UserDto();
        userDto.setEmail(userEmail);
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        message = "Testing message";
    }

    @Test
    @DisplayName("Send email: check execution")
    void testSend() {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(userDto.getEmail());
        emailMessage.setSubject(message);
        Mockito.when(emailServiceHandler.getSipmleMailMessage(userDto, message)).thenReturn(emailMessage);

        emailService.send(userDto, message);
        verify(javaMailSender, times(1)).send(mailMessageCaptor.capture());
    }

    @Test
    @DisplayName("Get preferred contact: check execution")
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = emailService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.EMAIL, preferredContact);
    }
}
