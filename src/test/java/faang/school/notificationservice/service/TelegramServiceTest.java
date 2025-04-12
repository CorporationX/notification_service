package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.TelegramMessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private TelegramClient telegramClient;

    private TelegramService telegramService;

    @BeforeEach
    void setUp() {
        telegramService = new TelegramService("test-token");
        ReflectionTestUtils.setField(telegramService, "telegramClient", telegramClient);
    }

    @Test
    void testSendShouldCallExecuteWithCorrectMessage() throws TelegramApiException {
        Long chatId = 12345L;
        String messageText = "Hello";
        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(chatId);

        telegramService.send(user, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramClient, times(1)).execute(captor.capture());

        SendMessage captured = captor.getValue();
        assertEquals(chatId.toString(), captured.getChatId());
        assertEquals(messageText, captured.getText());
    }

    @Test
    void testSendShouldThrowTelegramMessageExceptionWhenTelegramApiExceptionIsThrown() throws TelegramApiException {
        Long chatId = 12345L;
        String messageText = "Test";
        UserDto user = mock(UserDto.class);
        when(user.getId()).thenReturn(chatId);

        doThrow(new TelegramApiException("API Error")).when(telegramClient).execute(any(SendMessage.class));

        TelegramMessageException exception = assertThrows(TelegramMessageException.class, () -> {
            telegramService.send(user, messageText);
        });

        assertTrue(exception.getMessage().contains("Error when sending a message to the user"));
    }

    @Test
    void getPreferredContactShouldReturnTelegram() {
        UserDto.PreferredContact result = telegramService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.TELEGRAM, result);
    }
}
