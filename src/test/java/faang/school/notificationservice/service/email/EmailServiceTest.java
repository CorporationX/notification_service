package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailService emailService;
    @Captor
    ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor;

    private final UserDto userDto = new UserDto();
    private String mailText;

    @BeforeEach
    void init(){
        userDto.setEmail("test@mail.ru");
        mailText = "mailText";
    }

    @Test
    void testSend() {
        emailService.send(userDto, mailText);

        verify(javaMailSender).send(simpleMailMessageArgumentCaptor.capture());
        SimpleMailMessage capturedArgument =  simpleMailMessageArgumentCaptor.getValue();

        assertEquals(mailText, capturedArgument.getText());
        assertEquals(1, capturedArgument.getTo().length);
        assertEquals(userDto.getEmail(), capturedArgument.getTo()[0]);
    }

    @Test
    void testGetPreferredContact() {
        var result = emailService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.EMAIL, result);
    }
}