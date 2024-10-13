package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.telegram.TelegramConfig;
import faang.school.notificationservice.dto.user.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @InjectMocks
    private TelegramService telegramService;

    @Mock
    private TelegramConfig telegramConfig;

    @Test
    @DisplayName("Method should return expected value")
    void whenCallThenReturnExpectedValue() {
        assertEquals(UserDto.PreferredContact.TELEGRAM, telegramService.getPreferredContact());
    }

}
