package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMessage(
            String toAddress,
            String subject,
            String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@x.com");
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void sendMessageWithAttachment(
            String toAddress,
            String subject,
            String text,
            String pathToAttachment) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@baeldung.com");
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Attachment", file);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessageWithInputStreamAttachment(
            String toAddress,
            String subject,
            String text,
            String attachmentName,
            InputStream attachmentStream) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@baeldung.com");
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text);

            helper.addAttachment(attachmentName, new InputStreamResource(attachmentStream));

            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error sending email with InputStreamAttachment", e);
        }
    }
}
