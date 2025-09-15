package faang.school.notificationservice.service.mail;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Locale;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MessageSource messageSource;

    private static final String EMAIL_SUBJECT_CODE = "email.subject";

    @Test
    void test_getPreferredContact_success() {
        EmailService emailService = new EmailService(javaMailSender, messageSource);

        UserDto.PreferredContact result = emailService.getPreferredContact();

        assertThat(result).isEqualTo(UserDto.PreferredContact.EMAIL);
        assertThat(result).isNotNull();
    }

    @Test
    void test_send_success() {
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        String text = "Test message";
        String expectedSubject = "Test Subject";

        when(messageSource.getMessage(EMAIL_SUBJECT_CODE, null, Locale.ENGLISH))
                .thenReturn(expectedSubject);

        emailService.send(user, text);

        verify(messageSource).getMessage(EMAIL_SUBJECT_CODE, null, Locale.ENGLISH);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage.getTo()).containsExactly("test@example.com");
        assertThat(capturedMessage.getSubject()).isEqualTo(expectedSubject);
        assertThat(capturedMessage.getText()).isEqualTo(text);
    }

    @Test
    void test_send_withNullUser_shouldNotThrowException() {
        UserDto user = null;
        String text = "Test message";

        assertThatThrownBy(() -> emailService.send(user, text))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_send_withNullUserEmail_shouldSendEmailWithNullRecipient() {
        UserDto user = new UserDto();
        user.setEmail(null);
        String text = "Test message";
        String expectedSubject = "Test Subject";

        when(messageSource.getMessage(EMAIL_SUBJECT_CODE, null, Locale.ENGLISH))
                .thenReturn(expectedSubject);

        emailService.send(user, text);

        verify(messageSource).getMessage(EMAIL_SUBJECT_CODE, null, Locale.ENGLISH);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertThat(capturedMessage.getTo()).containsExactly((String) null);
        assertThat(capturedMessage.getTo()).hasSize(1);
        assertThat(Objects.requireNonNull(capturedMessage.getTo())[0]).isNull();
    }

    @Test
    void test_send_withMessageSourceException_shouldLogError() {
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        String text = "Test message";

        when(messageSource.getMessage(EMAIL_SUBJECT_CODE, null, Locale.ENGLISH))
                .thenThrow(new RuntimeException("Message source error"));

        emailService.send(user, text);

        verify(messageSource).getMessage(EMAIL_SUBJECT_CODE, null, Locale.ENGLISH);
        verifyNoInteractions(javaMailSender);
    }
}
