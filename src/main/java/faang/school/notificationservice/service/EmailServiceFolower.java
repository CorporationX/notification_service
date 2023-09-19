//package faang.school.notificationservice.service;
//
//import faang.school.notificationservice.dto.UserDto;
//import faang.school.notificationservice.model.PreferredContact;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class EmailServiceFolower implements NotificationService {
//
//    //private final DefaultEmailService defaultEmailService = new DefaultEmailService();
//    private final JavaMailSender mailSender;
//
//    @Override
//    public PreferredContact getPreferredContact() {
//        return PreferredContact.EMAIL;
//    }
//
//    @Override
//    public void send(UserDto folowee, String message) {
//
//        SimpleMailMessage messages = new SimpleMailMessage();
//        messages.setTo("myasnickoff.iw@yandex.ru");
//        messages.setSubject("subject");
//        messages.setText(message);
//        mailSender.send(messages);
//
//        //defaultEmailService.sendSimpleMail(folowee.getEmail(),"notifications",message);
//        //defaultEmailService.sendSimpleMail("myasnickoff.iw@yandex.ru","notifications",message);
//
//    }
//}
