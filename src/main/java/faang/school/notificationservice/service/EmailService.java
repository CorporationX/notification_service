//package faang.school.notificationservice.service;
//
//import faang.school.notificationservice.dto.UserDto;
//import faang.school.notificationservice.service.notification.NotificationService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
///**
// * @author Evgenii Malkov
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class EmailService implements NotificationService {
//
//    private final JavaMailSender sender;
//    @Value("${spring.mail.username}")
//    private String serviceMail;
//
//    @Override
//    public void send(UserDto user, String message) {
//        if (user.getEmail() == null || user.getEmail().isBlank()) {
//            throw new IllegalArgumentException("Not found email for userId: " + user.getId());
//        }
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom(serviceMail);
//        mailMessage.setTo(user.getEmail());
//        mailMessage.setText(message);
//        sender.send(mailMessage);
//        log.info("Sent message to: {}, userId: {}", user.getEmail(), user.getId());
//    }
//
//    @Override
//    public UserDto.PreferredContact getPreferredContact() {
//        return UserDto.PreferredContact.EMAIL;
//    }
//
//}
