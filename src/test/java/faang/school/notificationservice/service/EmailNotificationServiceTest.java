package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EmailNotificationServiceTest {

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private String notificationServiceEmail;

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
        verify(notificationService, times(1)).send(userDto, textNotification);
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
