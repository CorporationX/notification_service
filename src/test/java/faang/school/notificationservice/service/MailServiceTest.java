package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Отправка письма: пользователь указал предпочтение EMAIL")
    void send_ShouldSendEmail_WhenUserHasEmailPreference() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("TestUser");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        String text = "Это тестовое уведомление.";

        emailService.send(userDto, text);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("test@example.com", sentMessage.getTo()[0]);
        assertEquals("Новое уведомление от NotificationService", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Это тестовое уведомление."));
    }

    @Test
    @DisplayName("Отправка письма: предпочтение пользователя не указано (используется EMAIL по умолчанию)")
    void send_ShouldFallbackToEmail_WhenUserPreferenceIsNull() {
        UserDto userDto = new UserDto();
        userDto.setEmail("fallback@example.com");
        userDto.setUsername("FallbackUser");
        String text = "Тест fallback.";

        emailService.send(userDto, text);

        assertEquals(UserDto.PreferredContact.EMAIL, userDto.getPreferredContact());
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("fallback@example.com", sentMessage.getTo()[0]);
    }

    @Test
    @DisplayName("Обработка ошибки: отправка письма вызвала исключение MailException")
    void send_ShouldHandleMailException_WhenMailSenderThrowsException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("error@example.com");
        userDto.setUsername("ErrorUser");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        String text = "Тестовая ошибка.";

        doThrow(new MailException("Mocked mail error") {}).when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.send(userDto, text));

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Отправка письма: email пользователя отсутствует")
    void send_ShouldNotSendEmail_WhenUserEmailIsNull() {
        UserDto userDto = new UserDto();
        userDto.setEmail(null);
        userDto.setUsername("NoEmailUser");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        String text = "Тест без email.";

        emailService.send(userDto, text);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}
