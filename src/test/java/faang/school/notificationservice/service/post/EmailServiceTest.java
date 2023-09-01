package faang.school.notificationservice.service.post;

import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.entity.PreferredContact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private SimpleMailMessage message;
    @Value("${companyName}")
    private String subject;
    private String text = "text";
    private MentorshipOfferedEventDto mentorshipOfferedEventDto = new MentorshipOfferedEventDto();


    @Test
    public void testIsPreferredContact() {
        mentorshipOfferedEventDto.setPreferredContact(PreferredContact.PHONE);
        assertFalse(emailService.isPreferredContact(mentorshipOfferedEventDto));

        mentorshipOfferedEventDto.setPreferredContact(PreferredContact.EMAIL);
        assertTrue(emailService.isPreferredContact(mentorshipOfferedEventDto));
    }

    @Test
    public void testSend() {
        mentorshipOfferedEventDto.setEmail("email");
        emailService.send(mentorshipOfferedEventDto, text);

        Mockito.verify(message, Mockito.times(1))
                .setTo(mentorshipOfferedEventDto.getEmail());
        Mockito.verify(message, Mockito.times(1))
                .setSubject(subject);
        Mockito.verify(message, Mockito.times(1))
                .setText(text);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(message);
    }
}