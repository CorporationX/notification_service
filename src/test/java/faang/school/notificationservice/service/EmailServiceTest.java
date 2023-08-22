package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;

    @Test
    void testSendMail() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail("some@email.com");
        user.setPreference(UserDto.PreferredContact.SMS);
        String text = "text";

        emailService.send(user, text);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}