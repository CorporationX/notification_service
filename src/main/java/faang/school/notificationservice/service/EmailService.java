package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.validator.EmailServiceValidator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final EmailServiceValidator emailServiceValidator;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto user, String message) {
        emailServiceValidator.checkUserDtoIsNull(user);
        emailServiceValidator.checkMessageIsNull(message);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setText(message);

        javaMailSender.send(simpleMailMessage);
    }

    public void sendMessageWithAttachment(UserDto user, String message, String pathAttachment) {
        emailServiceValidator.checkUserDtoIsNull(user);
        emailServiceValidator.checkMessageIsNull(message);
        emailServiceValidator.checkPathIsNull(pathAttachment);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            FileSystemResource file = new FileSystemResource(new File(pathAttachment));
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText(message);
            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);
    }
}