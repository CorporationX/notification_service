package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import faang.school.notificationservice.validator.EmailServiceValidator;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailServiceValidator emailServiceValidator;

    private UserDto userDto;
    private String message;
    private String pathAttachment;
    private MimeMessage mimeMessage;
    private String messagesHeader;

    @BeforeEach
    public void setUp() {
        mimeMessage = mock(MimeMessage.class);
        userDto = UserDto.builder().email("email").id(1L).username("username").build();
        message = "message";
        pathAttachment = "pathAttachment";
        messagesHeader = "message";
    }

    @Test
    public void testGetPreferredContact() {
        UserDto.PreferredContact preferredExpected = UserDto.PreferredContact.EMAIL;

        assertEquals(preferredExpected, emailService.getPreferredContact());
    }

    @Test
    public void testSend() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userDto.getEmail());
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject(messagesHeader);

        emailService.send(userDto, message, messagesHeader);

        verify(emailServiceValidator, times(1)).checkUserDtoIsNull(userDto);
        verify(emailServiceValidator, times(1)).checkMessageIsNull(message);
        verify(javaMailSender, times(1)).send(simpleMailMessage);
    }

    @Test
    public void testSendMessageWithAttachment() {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendMessageWithAttachment(userDto, message, pathAttachment);

        verify(emailServiceValidator, times(1)).checkUserDtoIsNull(userDto);
        verify(emailServiceValidator, times(1)).checkMessageIsNull(message);
        verify(emailServiceValidator, times(1)).checkPathIsNull(pathAttachment);
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}