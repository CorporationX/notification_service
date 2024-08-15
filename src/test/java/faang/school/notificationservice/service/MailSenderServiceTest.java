package faang.school.notificationservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserDto.PreferredContact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceTest {

  @Mock
  private JavaMailSender javaMailSender;

  @InjectMocks
  private EmailSenderService mailSenderService;

  private UserDto getUserDto() {
    return UserDto.builder()
        .email("user@gmail.com")
        .build();
  }

  private SimpleMailMessage getMailMessage() {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom("noreply@faang-school.com");
    simpleMailMessage.setTo(getUserDto().getEmail());
    simpleMailMessage.setSubject("FAANG school");
    simpleMailMessage.setText("messageText");
    return simpleMailMessage;
  }

  @BeforeEach
  public void setUp() {
    mailSenderService.setSubject("FAANG school");
    mailSenderService.setSentFrom("noreply@faang-school.com");
  }

  @Test
  @DisplayName("Проверка отправки сообщения")
  public void testSendWhenInputData() {
    mailSenderService.send(getUserDto(), "messageText");

    verify(javaMailSender).send(getMailMessage());
  }

  @Test
  @DisplayName("Проверка предпочтительного контакта")
  void testGetPreferredContact() {
    PreferredContact expectedResult = PreferredContact.EMAIL;

    PreferredContact result = mailSenderService.getPreferredContact();

    assertEquals(expectedResult, result);
  }
}