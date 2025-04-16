package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
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
    void testSendMessageThrowsTelegramApiException() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(123456789);
        String message = "Hello, World!";
        SendMessage sendMessage = SendMessage.builder().chatId(user.getId()).text(message).build();

        when(telegramClient.execute(sendMessage)).thenThrow(new TelegramApiException("API Error"));

        TelegramApiException exception = assertThrows(TelegramApiException.class, () -> {
            telegramService.send(user, message);
        });

        assertTrue(exception.getMessage().contains("API Error"));
    }

    @Test
    void getPreferredContactShouldReturnTelegram() {
        UserDto.PreferredContact result = telegramService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.TELEGRAM, result);
    }
}



