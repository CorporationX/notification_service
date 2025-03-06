package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.impl.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    private UserDto user;
    private final String MESSAGE = "test message";

    @BeforeEach
    void setUp() {
        user = UserDto.builder()
                .id(1L)
                .username("test name")
                .email("example@example.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }

    @Test
    public void testSendEmailSuccess() {
        emailService.send(user, MESSAGE);
        ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(mailMessageCaptor.capture());

        SimpleMailMessage capturedMessage = mailMessageCaptor.getValue();

        Assertions.assertEquals("test name", capturedMessage.getFrom());
        Assertions.assertEquals("example@example.com", capturedMessage.getTo()[0]);
        Assertions.assertEquals("test message", capturedMessage.getText());
    }
}