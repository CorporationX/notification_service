package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void positiveSendEmail() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("example@gmail.com")
                .build();
        String message = "Повторное сообщение";
        emailService.send(userDto, message);
    }
}