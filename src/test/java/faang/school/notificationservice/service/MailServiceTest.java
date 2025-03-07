package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserNotificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {
    @Mock
    private JavaMailSenderImpl mailSender;
    @InjectMocks
    private MailService mailService;

    @Test
    public void send_ShouldSendMessage() {
        UserNotificationDto userDto = new UserNotificationDto();
        userDto.setEmail("whatever@gmail.com");
        mailService.send(userDto, "Hello, whatever!");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void getPreferredContact_ShouldReturnEmail() {
        assertEquals(UserNotificationDto.PreferredContact.EMAIL, mailService.getPreferredContact());
    }
}