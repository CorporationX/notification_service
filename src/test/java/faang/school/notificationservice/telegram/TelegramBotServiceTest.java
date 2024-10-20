package faang.school.notificationservice.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramBotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramBotServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramBotService telegramBotService;

    @Captor
    private ArgumentCaptor<SendMessage> sendMessageCaptor;

    @Test
    void testSendMessage() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("Denis");
        user.setPreference(UserDto.PreferredContact.TELEGRAM);
        user.setTelegramId(1L);
        String message = "Test message";

        telegramBotService.send(user, message);

        verify(telegramBot, times(1)).execute(sendMessageCaptor.capture());

        SendMessage capturedMessage = sendMessageCaptor.getValue();
        assertThat(capturedMessage.getChatId()).isEqualTo("1");
        assertThat(capturedMessage.getText()).isEqualTo(message);
    }

    @Test
    void testPreferenceIsNotTelegram() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(2L);
        user.setUsername("Marina");
        user.setPreference(UserDto.PreferredContact.EMAIL);
        user.setTelegramId(9L);
        String message = "This message will not be sent Telegram.";

        assertThrows(IllegalArgumentException.class, () -> {
            telegramBotService.send(user, message);
        });
        verify(telegramBot, never()).execute(any(SendMessage.class));
    }

    @Test
    void testTelegramIdIsNull() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(3L);
        user.setUsername("Pavel");
        user.setPreference(UserDto.PreferredContact.TELEGRAM);
        user.setTelegramId(null);
        String message = "This message should not be sent because Telegram ID is null.";

        assertThrows(IllegalArgumentException.class, () -> {
            telegramBotService.send(user, message);
        });
        verify(telegramBot, never()).execute(any(SendMessage.class));
    }

    @Test
    void testGetPreferredContact_ShouldReturnTelegram() {
        UserDto.PreferredContact preferredContact = telegramBotService.getPreferredContact();

        assertThat(preferredContact).isEqualTo(UserDto.PreferredContact.TELEGRAM);
    }
}
