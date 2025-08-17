package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegram.TelegramServiceBot;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private TelegramServiceBot telegramServiceBot;

    @InjectMocks
    private TelegramService telegramService;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(123L);
        user.setUsername("testuser");
    }

    @Test
    void sendShouldSendMessageSuccessfully() {
        String message = "Test notification";

        telegramService.send(user, message);

        verify(telegramServiceBot).sendMessage(123L, message);
    }

    @Test
    void sendShouldThrowExceptionWhenBotFails() {
        String message = "Test notification";
        RuntimeException exception = new RuntimeException("Failed to send telegram message");
        doThrow(exception).when(telegramServiceBot).sendMessage(123L, message);

        assertThatThrownBy(() -> telegramService.send(user, message))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to send telegram message");

        verify(telegramServiceBot).sendMessage(123L, message);
    }

    @Test
    void getPreferredContactShouldReturnTelegram() {
        UserDto.PreferredContact result = telegramService.getPreferredContact();

        assertThat(result).isEqualTo(UserDto.PreferredContact.TELEGRAM);
    }
}