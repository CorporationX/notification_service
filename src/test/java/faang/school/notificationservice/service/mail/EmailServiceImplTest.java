package faang.school.notificationservice.service.mail;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @Mock
    private JavaMailSender emailSender;
    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void shouldSendMessage() {
        UserDto userDto = UserDto.builder()
                .email("test@mail")
                .build();
        String text = "testText";

        emailService.send(userDto, text);

        Mockito.verify(emailSender).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    void getPreferredContact() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }
}