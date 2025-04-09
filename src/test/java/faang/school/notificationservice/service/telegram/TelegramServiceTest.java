package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @InjectMocks
    private TelegramService telegramService;

    @Mock
    private MyTelegramBot mockBot;


    @Test
    void shouldSendMessageSuccessfully() throws Exception {
        UserDto user = new UserDto();
        user.setTelegramChatId("123456789");

        telegramService.send(user, "Hello from test!");

        verify(mockBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void shouldThrowWhenTelegramIdIsBlank() {
        UserDto user = new UserDto();
        user.setTelegramChatId("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            telegramService.send(user, "message");
        });

        assertEquals("User does not have a Telegram ID", ex.getMessage());
    }

    @Test
    void shouldThrowNotificationExceptionOnApiFailure() throws Exception {
        UserDto user = new UserDto();
        user.setTelegramChatId("123456789");

        doThrow(new TelegramApiException("fail")).when(mockBot).execute(any(SendMessage.class));

        NotificationException ex = assertThrows(NotificationException.class, () -> {
            telegramService.send(user, "test");
        });

        assertEquals("Failed to send Telegram message", ex.getMessage());
    }

    @Test
    void getPreferredContactShouldReturnTelegram() {
        assertEquals(UserDto.PreferredContact.TELEGRAM, telegramService.getPreferredContact());
    }
}
