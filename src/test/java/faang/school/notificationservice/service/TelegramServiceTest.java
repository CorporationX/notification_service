package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.TelegramChatIdNotFound;
import faang.school.notificationservice.service.bot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = new UserDto();
    }

    @Test
    void testSend_IfChatIdIsNotNull() {
        user.setTelegramChatId(12345L);
        String message = "Test message";

        telegramService.send(user, message);

        verify(telegramBot, times(1)).sendMessage(user.getTelegramChatId(), message);
    }

    @Test
    void testSendShouldThrowException_IfChatIdIsNull() {
        user.setTelegramChatId(null);
        String message = "Test message";

        assertThrows(TelegramChatIdNotFound.class, () -> telegramService.send(user, message));

        verify(telegramBot, times(0)).sendMessage(anyLong(), anyString());
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = telegramService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.TELEGRAM, preferredContact);
    }
}