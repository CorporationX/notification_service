package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.EmailNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailNotificationService emailService;
    @Mock
    private JavaMailSender mailSender;
    UserDto user;
    SimpleMailMessage message;

    @BeforeEach
    void init() {
        user = UserDto.builder()
                .id(1L)
                .username("UserFirst")
                .email("firstUser@google.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("CorporationX");
        message.setText("text");
    }

    @Test
    void successGetPreferredContact() {
        UserDto.PreferredContact expected = UserDto.PreferredContact.EMAIL;
        UserDto.PreferredContact actual = emailService.getPreferredContact();
        assertEquals(expected, actual);
    }

    @Test
    void successSendNotification() {
        emailService.send(user, "text");
        verify(mailSender, times(1)).send(message);
    }
}