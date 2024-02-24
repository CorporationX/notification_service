package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @InjectMocks
    private EmailServiceImpl emailService;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private SimpleMailMessage message;

    @Test
    void testSendSuccessful() {
        UserDto userDto = UserDto.builder()
                .email("user@gmail.com")
                .build();
        String text = "text";

        message.setTo(userDto.getEmail());
        message.setText(text);
        message.setSubject("test");

        javaMailSender.send(message);
        emailService.send(userDto, text);
    }
}