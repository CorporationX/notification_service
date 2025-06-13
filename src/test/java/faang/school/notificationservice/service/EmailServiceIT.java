package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceIT {

    @Autowired
    private EmailService emailService;

    @Test
    @Disabled("Интеграционный тест: для проверки реальной отправки")
    void test_SendEmailToRealMailbox() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail("triton.stream10@gmail.com");

        emailService.send(user, "Тест сообщение от notification_service");
    }
}