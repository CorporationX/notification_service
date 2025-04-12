package faang.school.notificationservice.service;

import faang.school.notificationservice.config.app.TelegramProperties;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TelegramServiceTest {
    private TelegramService telegramService;
    private TelegramProperties telegramProperties;

    @BeforeEach
    void setUp(){
        telegramProperties = mock(TelegramProperties.class);
        telegramService = spy(new TelegramService(telegramProperties));
    }

    @Test
    void shouldSendMessageSuccessfully() throws Exception {
        UserDto user = new UserDto();
        user.setTelegramId(123456L);
        user.setId(1L);

        String message = "Hello world";

        doReturn(null).when(telegramService).execute(any(SendMessage.class));

        telegramService.send(user, message);

        verify(telegramService, times(1))
                .execute(argThat((SendMessage msg) ->
                        msg.getChatId().equals("123456") &&
                                msg.getText().equals("Hello world")
                ));
    }

    @Test
    void shouldLogErrorWhenTelegramFails() throws Exception{
        UserDto user = new UserDto();
        user.setTelegramId(111L);
        user.setId(1L);

        String message = "fail";

        doThrow(new TelegramApiException("API Down")).when(telegramService).execute(any(SendMessage.class));
        telegramService.send(user, message);
        verify(telegramService, times(1)).execute(any(SendMessage.class));

    }

    @Test
    void shouldReturnPreferredContactTelegram() {
        assert telegramService.getPreferredContact() == UserDto.PreferredContact.TELEGRAM;
    }
    @Test
    void shouldReturnTelegramUsernameFromProperties() {
        when(telegramProperties.getUsername()).thenReturn("my_bot");
        assert telegramService.getBotUsername().equals("my_bot");
    }

    @Test
    void shouldReturnTelegramTokenFromProperties() {
        when(telegramProperties.getToken()).thenReturn("123456");
        assert telegramService.getBotToken().equals("123456");
    }

}
