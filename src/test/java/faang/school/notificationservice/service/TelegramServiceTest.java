package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.telegram.MyTelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TelegramServiceTest {

    private MyTelegramBot myTelegramBot;
    private TelegramService telegramService;

    @BeforeEach
    void setup() {
        myTelegramBot = mock(MyTelegramBot.class);
        telegramService = new TelegramService(myTelegramBot);
    }

    @Test
    void shouldSendMessageToTelegramUser() throws Exception {

        UserDto user = new UserDto();
        user.setTelegramId(123456789L);
        String message = "Test message";

        telegramService.send(user, message);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(myTelegramBot, times(1)).execute(captor.capture());

        SendMessage actual = captor.getValue();
        assertEquals("123456789", actual.getChatId());
        assertEquals(message, actual.getText());
    }

    @Test
    void shouldThrowExceptionWhenTelegramFails() throws Exception {

        UserDto user = new UserDto();
        user.setTelegramId(123456789L);

        doThrow(new RuntimeException("Telegram error"))
                .when(myTelegramBot)
                .execute(any(SendMessage.class));

        assertThrows(RuntimeException.class, () -> telegramService.send(user, "Message"));
    }
}