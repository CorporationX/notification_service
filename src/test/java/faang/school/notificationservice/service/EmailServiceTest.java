package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationException;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование сервиса отправки email")
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    @DisplayName("Успешная отправка email при корректных параметрах")
    void givenValidUserAndMessage_whenSendEmail_thenEmailIsSent() {
        UserDto user = new UserDto();
        user.setEmail("mail@gmail.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);
        String message = "Test message";

        emailService.send(user, message);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Выброс исключения при ошибке отправки email")
    void givenValidUserAndMessage_whenSendEmailFails_thenThrowNotificationException() {
        UserDto user = new UserDto();
        user.setEmail("mail@gmail.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);
        String message = "Test message";

        doThrow(new RuntimeException("SMTP error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(NotificationException.class, () ->
                emailService.send(user, message));
    }

    @Test
    @DisplayName("Получение предпочитаемого способа связи - EMAIL")
    void whenGetPreferredContact_thenReturnEmailPreference() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }
}