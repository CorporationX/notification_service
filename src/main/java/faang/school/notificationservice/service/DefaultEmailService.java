//package faang.school.notificationservice.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.util.Properties;
//
//@Component
//public class DefaultEmailService implements EmailService {
//    @Value("${spring.mail.port}")
//    private int mailPort;
//    @Value("${spring.mail.host}")
//    private String mailHost;
//    @Value("${spring.mail.username}")
//    private String mailUserName;
//    @Value("${spring.mail.password}")
//    private String mailUserPassword;
//    //private final JavaMailSender javaMailSender;
//
//    @Override
//    public void sendSimpleMail(String toAddress, String subject, String message) {
//         JavaMailSender javaMailSender = getJavaMailSender();
//
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo(toAddress);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(message);
//
//        javaMailSender.send(simpleMailMessage);
//    }
//
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(mailHost);
//        mailSender.setPort(mailPort);
//
//        mailSender.setUsername(mailUserName);
//        mailSender.setPassword(mailUserPassword);
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//
//}
