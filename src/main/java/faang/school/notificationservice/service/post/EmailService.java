package faang.school.notificationservice.service.post;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.entity.PreferredContact;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;
    private String subject="CorporationX";

    @Override
    public boolean isPreferredContact(MentorshipOfferedEventDto mentorshipOfferedEventDto) {
        return mentorshipOfferedEventDto.getPreferredContact().equals(PreferredContact.EMAIL);
    }

    @Override
    public void send(MentorshipOfferedEventDto mentorshipOfferedEventDto, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mentorshipOfferedEventDto.getEmail());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
