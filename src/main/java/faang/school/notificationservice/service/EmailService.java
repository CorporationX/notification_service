package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements NotificationService{

    private final JavaMailSender mailSender;

    /**
     * Отправка уведомления на email пользователя.
     *
     * @param userDto объект пользователя, у которого есть email
     * @param subject тема письма
     * @param message текст письма
     */
    public void send(UserDto userDto, String subject, String message) {
        String string = "shherbakov99ilya@mail.ru";
        //   if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            //       throw new IllegalArgumentException("User email is missing");
            //   }

        SimpleMailMessage mail = new SimpleMailMessage();
       // mail.setTo(userDto.getEmail());
        mail.setTo(string);
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);
    }

    @Override
    public void send(UserDto userDto, String message) {
        send(userDto, "Notification", message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
