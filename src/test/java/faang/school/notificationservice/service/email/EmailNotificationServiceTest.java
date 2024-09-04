package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailNotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailNotificationService notificationService;

    private String notificationServiceEmail = "some@gmail.com";

    @BeforeEach
    void setup(){
        notificationService = new EmailNotificationService(
                mailSender,
                notificationServiceEmail
        );
    }

    @Test
    public void testSendNotification() {
        String textNotification = "This is a test";
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("johndoe@example.com")
                .username("JohnDoe")
                .phone("1234567890")
                .build();

        SimpleMailMessage mail = getMessage(userDto, textNotification);

        doNothing().when(mailSender).send(mail);
        notificationService.send(userDto, textNotification);
        verify(mailSender, timeout(1)).send(mail);
    }

    private SimpleMailMessage getMessage(UserDto user, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(notificationServiceEmail);
        mail.setTo(user.getEmail());
        mail.setText(text);

        return mail;
    }

    // real send message
//    @Test
//    public void testSendEmailNotification() {
//        String textNotification = "This is a test";
//        UserDto userDto = UserDto.builder()
//                .id(1L)
//                .email("markretcher@gmail.com")
//                .username("Mark")
//                .build();
//
//        notificationService.send(userDto, textNotification);
//
//        assertTrue(true);
//    }
}
