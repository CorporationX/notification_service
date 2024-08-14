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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestEmailService {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@gmail.com");
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
    }

    @Test
    public void testSend() {
        String message = "massage";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userDto.getEmail());
        simpleMailMessage.setText(message);

        emailService.send(userDto, message);

        verify(mailSender, times(1)).send(simpleMailMessage);
    }

    @Test
    public void testGetPreferredContact() {
        UserDto.PreferredContact result = emailService.getPreferredContact();
        assertEquals(userDto.getPreference(), result);
    }
}