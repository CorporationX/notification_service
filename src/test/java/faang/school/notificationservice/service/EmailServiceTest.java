package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    @DisplayName("Инициализация моков для тестирования EmailService")
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Отправка email успешно с правильными данными")
    void shouldSendEmailSuccessfully() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("Test User");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        String notificationText = "This is a test notification.";

        emailService.send(userDto, notificationText);

        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertEquals("test@example.com", capturedMessage.getTo()[0]);
        assertEquals("Новое уведомление от NotificationService", capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains(notificationText));
    }

    @Test
    @DisplayName("Установка предпочтения EMAIL по умолчанию, если оно не указано")
    void shouldSetDefaultPreferenceWhenNull() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("Test User");
        userDto.setPreference(null);

        String notificationText = "This is a test notification.";

        emailService.send(userDto, notificationText);

        assertEquals(UserDto.PreferredContact.EMAIL, userDto.getPreferredContact());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Обработка исключения MailException при отправке email")
    void shouldHandleMailException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("Test User");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        String notificationText = "This is a test notification.";

        doThrow(new MailException("Failed to send email") {}).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.send(userDto, notificationText);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
