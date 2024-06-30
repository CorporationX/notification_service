package faang.school.notificationservice.service;

import faang.school.notificationservice.config.notification.EmailProperties;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailProperties emailProperties;

    private UserDto userDto;


    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("albert@yandex.ru")
                .phone("134124124")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }

    @Nested
    class PositiveTest {
        @Test
        void send() {
            String text = "text";
            String username = "p1mafka@gmail.com";
            when(emailProperties.getUsername()).thenReturn(username);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(username);
            message.setTo(userDto.getEmail());
            message.setSubject("CorporationX");
            message.setText(text);
            emailService.send(userDto, text);
            verify(javaMailSender).send(message);
        }

        @Test
        void getPreferredContact() {
            assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
        }
    }
}