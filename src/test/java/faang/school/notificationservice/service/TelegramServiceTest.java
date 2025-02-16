package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.TelegramBot;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    @Captor
    private ArgumentCaptor<SendMessage> messageCaptor;

    private UserDto userDto;
    private String message;

    @BeforeEach
    public void beforeEach() {
        userDto = new UserDto();
        userDto.setId(123L);
        userDto.setPreference(UserDto.PreferredContact.TELEGRAM);

        message = "Test message";
    }

    @Test
    public void sendSuccessCase() throws TelegramApiException {
        telegramService.send(userDto, message);

        Mockito.verify(telegramBot, Mockito.times(1)).execute(messageCaptor.capture());

        assertEquals(userDto.getId(), Long.parseLong(messageCaptor.getValue().getChatId()));
        assertEquals(message, messageCaptor.getValue().getText());
    }
}
