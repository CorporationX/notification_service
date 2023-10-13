package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
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
    @InjectMocks
    private EmailService emailService;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private SimpleMailMessage simpleMailMessage;

    @Test
    void sendNotification() {
        Long receiverId = 1L;
        UserDto userDto = UserDto.builder()
                .id(receiverId)
                .email("test")
                .build();
        String message = "test";

        simpleMailMessage.setTo(userDto.getEmail());
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject("test");

        Mockito.when(userServiceClient.getUser(receiverId))
                .thenReturn(userDto);

        emailService.sendNotification(receiverId, message);

        Mockito.verify(userServiceClient, Mockito.times(1))
                .getUser(receiverId);
        Mockito.verify(javaMailSender, Mockito.times(1))
                .send(simpleMailMessage);
    }
}