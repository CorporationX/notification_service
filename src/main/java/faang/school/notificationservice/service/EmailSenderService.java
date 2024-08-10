package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService implements NotificationService {

  private static final String EMAIL_MESSAGE_SENDING = "Email message send to email address: {}";

  private final JavaMailSender mailSender;

  @Setter
  @Value("${spring.mail.default_subject}")
  private String subject;

  @Setter
  @Value("${spring.mail.sender.email}")
  private String sentFrom;

  @Override
  public void send(UserDto user, String message) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(user.getEmail());
    simpleMailMessage.setFrom(sentFrom);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(message);
    mailSender.send(simpleMailMessage);
    log.info(EMAIL_MESSAGE_SENDING, user.getEmail());
  }

  @Override
  public UserDto.PreferredContact getPreferredContact() {
    return UserDto.PreferredContact.EMAIL;
  }

}
