package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender emailSender;

    private static final String EMAIL = "stepnova.varya@yandex.ru";
    private static final String MESSAGE = "message";
    private static final String SUBJECT = "subject";

    private UserDto user;
    private SimpleMailMessage mailMessage;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(emailService, "subject", SUBJECT);
        user = new UserDto();
        user.setEmail(EMAIL);

        mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(SUBJECT);
        mailMessage.setText(MESSAGE);
    }

    @Test
    @DisplayName("Успешная отправка email")
    public void testSend() {
        emailService.send(user, MESSAGE);

        verify(emailSender).send(mailMessage);
    }
}