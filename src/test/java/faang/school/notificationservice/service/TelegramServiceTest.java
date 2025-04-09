package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.TelegramBot;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    private UserDto user;
    private final long CHAT_ID = 12345L;
    private final String MESSAGE = "Test message";

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setTelegramChatId(CHAT_ID);
        user.setPreference(UserDto.PreferredContact.TELEGRAM);
    }

    @Test
    void givenUserDto_whenSendMessage_thenSuccess() throws TelegramApiException {
        telegramService.send(user, MESSAGE);

        verify(telegramBot).sendMessage(CHAT_ID, MESSAGE);
    }

    @Test
    void givenUserDto_whenSendMessage_thenNotSendMessage() {
        user.setPreference(UserDto.PreferredContact.EMAIL);

        telegramService.send(user, MESSAGE);

        verifyNoInteractions(telegramBot);
    }
}
