package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Spy
    EmailService emailService;
    @InjectMocks
    EmailNotificationService emailNotificationService;

    @Test
    void send() {

        UserDto user = UserDto.builder()
                .id(1L)
                .email("user@mail.ru")
                .username("testUser")
                .build();
        String message = "Test message";

        emailNotificationService.send(user, message);
        Mockito.verify(emailService, Mockito.times(1))
                .sendSimpleMessage(user.getEmail(), "", message);

    }


}