package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor;

    private UserDto userDto;
    private String message;

    @BeforeEach
    public void setUp(){
        String userEmail = "user@mail.com";
        userDto = UserDto.builder()
                .email(userEmail)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        message = "message";
    }

    @Test
    @DisplayName("testing getPreferredContact method to return Email")
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = emailService.getPreferredContact();
        assertEquals(UserDto.PreferredContact.EMAIL, preferredContact);
    }

    @Test
    @DisplayName("testing send email method")
    void testSend() {
        emailService.send(userDto, message);
        verify(javaMailSender, times(1)).send(simpleMailMessageArgumentCaptor.capture());
    }
}