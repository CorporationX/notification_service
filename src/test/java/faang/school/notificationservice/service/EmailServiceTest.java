package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailService emailService;

    UserDto userDto;
    String message;
    UserDto.PreferredContact preferredContact;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        message = "Some message";
        preferredContact = UserDto.PreferredContact.EMAIL;
    }

    @Test
    void send() {
        emailService.send(userDto, message);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void getPreferredContact() {
        UserDto.PreferredContact result = emailService.getPreferredContact();

        assertEquals(preferredContact, result);
    }
}