package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageSendException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class ProstorSmsServiceIntTest {
    @Autowired
    ProstorSmsService prostorSmsService;
    private static final String PHONE = "79991234567";
    private static final String INVALID_PHONE = "12345";
    private static final String MESSAGE = "Hello";

    @Test
    @DisplayName("Успешный GET /send - status Accepted")
    void positive_whenSendCorrectRequest_returnsAccepted() {
        UserDto user = UserDto.builder()
                .phone(PHONE)
                .build();

        assertDoesNotThrow(() -> prostorSmsService.send(user, MESSAGE));
    }

    @Test
    @DisplayName("Ошибка GET /send - status Error")
    void positive_whenSendInvalidRequest_returnsError() {
        UserDto user = UserDto.builder()
                .phone(INVALID_PHONE)
                .build();

        assertThrows(MessageSendException.class,
                     () -> prostorSmsService.send(user, MESSAGE));
    }
}