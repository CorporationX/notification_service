package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.TelegramMessageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@SpringBootTest
class TelegramServiceTest {

    @MockBean
    private TelegramClient telegramClient;

    @Autowired
    private TelegramService telegramService;

    @Test
    void testSendMessageSuccess() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(123456789);
        String message = "Hello, World!";
        SendMessage sendMessage = SendMessage.builder().chatId(user.getId()).text(message).build();

        when(telegramClient.execute(sendMessage)).thenReturn(null);

        telegramService.send(user, message);

        verify(telegramClient, times(1)).execute(sendMessage);
    }

    @Test
    @DisplayName("Should throw TelegramMessageException When TelegramApiException Is Thrown")
    void testSendCheckingTheExclusionOfTelegramMessageException() throws TelegramApiException {
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



