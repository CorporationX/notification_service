package faang.school.notificationservice.service;

import faang.school.notificationservice.exception.TelegramNotificationException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.telegram.MyTelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

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

    @Test
    void shouldWrapTelegramApiExceptionIntoTelegramNotificationException() throws Exception {
        UserDto user = new UserDto();
        user.setTelegramId(123456789L);

        doThrow(new TelegramApiException("API error"))
                .when(myTelegramBot)
                .execute(any(SendMessage.class));

        TelegramNotificationException ex = assertThrows(
                TelegramNotificationException.class,
                () -> telegramService.send(user, "Message")
        );
        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof TelegramApiException);
        assertTrue(ex.getMessage().contains("123456789"));
    }

    @Test
    void getPreferredContactShouldBeTelegram() {
        assertEquals(UserDto.PreferredContact.TELEGRAM, telegramService.getPreferredContact());
    }

    @Test
    void shouldThrowIllegalArgumentIfTelegramIdIsNull() {
        UserDto user = new UserDto();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> telegramService.send(user, "msg"));
        assertTrue(ex.getMessage().contains("telegramId"));
    }


}