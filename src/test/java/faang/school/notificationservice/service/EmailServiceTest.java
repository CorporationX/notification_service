package faang.school.notificationservice.service;

import faang.school.notificationservice.config.email.EmailProperties;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    private static final Long ID = 1L;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Spy
    private EmailProperties emailProperties;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(ID)
                .email("random@mail.ru")
                .build();
    }


    @Test
    void shouldSendMail() {
        Mockito.doNothing().when(emailSender).send(messageCaptor.capture());

        emailService.send(userDto, "привет");

        Mockito.verify(emailSender).send(messageCaptor.capture());
    }

    @Test
    void shouldThrowSendMail() {
        var mailException = new MailException("Simulated MailException") {};
        Mockito.doThrow(mailException).when(emailSender).send(Mockito.any(SimpleMailMessage.class));
        Assertions.assertDoesNotThrow(() -> emailService.send(userDto, "привет"));
    }
}