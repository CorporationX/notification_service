package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private String text;

    private UserDto user;

    private SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    @BeforeEach
    void setUp() {
        text = "test";

        user = UserDto.builder()
                .email("email")
                .build();

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setText(text);
        simpleMailMessage.setSubject("CorporationX");
    }

    @Test
    void getPreferredContact_PreferenceShouldBeEmail() {
        Assertions.assertSame(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }

    @Test
    void send_MessageShouldBeFormed() {
        emailService.send(user, text);

        Mockito.verify(mailSender).send(simpleMailMessage);
    }
}
