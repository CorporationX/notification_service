package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {
    private TelegramService telegramService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        telegramService = Mockito.spy(new TelegramService());

        userDto = new UserDto();
        userDto.setId(123456L);
        userDto.setPreference(UserDto.PreferredContact.TELEGRAM);
    }

    @Test
    void testSendMessage_success() throws Exception {
        String text = "Hello from unit test!";
        SendMessage expectedMessage = new SendMessage("123456", text);

        doReturn(null).when(telegramService).execute(expectedMessage);

        telegramService.send(userDto, text);

        verify(telegramService, times(1)).execute(expectedMessage);
    }

    @Test
    void testSendMessage_throwsException() throws Exception {
        String text = "Oops!";
        SendMessage message = new SendMessage("123456", text);

        doThrow(new TelegramApiException("Forced failure")).when(telegramService).execute(message);

        assertThrows(RuntimeException.class, () -> telegramService.send(userDto, text));
    }
}
