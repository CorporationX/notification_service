package faang.school.notificationservice;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.enums.PreferredContact;
import faang.school.notificationservice.service.impl.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSend_Success() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setTelegramUserId("123456");
        user.setTelegramUsername("testUser");
        String message = "Test message";

        telegramService.send(user, message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("123456");
        sendMessage.setText(message);

        verify(telegramBot, times(1)).execute(sendMessage);
    }

    @Test
    public void testSend_TelegramApiException() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setTelegramUserId("123456");
        user.setTelegramUsername("testUser");
        String message = "Test message";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("123456");
        sendMessage.setText(message);

        doThrow(new TelegramApiException("Test Exception")).when(telegramBot).execute(sendMessage);

        telegramService.send(user, message);

        verify(telegramBot, times(1)).execute(sendMessage);
    }

    @Test
    public void testGetPreferredContact() {
        PreferredContact preferredContact = telegramService.getPreferredContact();

        assertEquals(PreferredContact.TELEGRAM, preferredContact);
    }
}
