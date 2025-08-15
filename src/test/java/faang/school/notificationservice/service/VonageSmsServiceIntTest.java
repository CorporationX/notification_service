package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class VonageSmsServiceIntTest {
    @Autowired
    private VonageSmsService vonageSmsService;
    private static final String PHONE = "9991234567";
    private static final String MESSAGE = "Hello from titan-stream-11";

    @Test
    @DisplayName("Успешный запрос OK на отправку SMS пользователю")
    void positive_whenSendMessage_returnsOk() {
        UserDto user = UserDto.builder()
                .phone(PHONE)
                .build();

        assertDoesNotThrow(() -> vonageSmsService.send(user, MESSAGE));
    }
}