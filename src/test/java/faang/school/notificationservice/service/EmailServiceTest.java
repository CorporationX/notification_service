package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @Disabled("Для ручного запуска с реальным Gmail")
    void positiveSendEmail() {
        if (System.getenv("CI") != null) {
            return;
        }
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("example@gmail.com")
                .build();
        String message = "Повторное сообщение";
        emailService.send(userDto, message);
    }
}
