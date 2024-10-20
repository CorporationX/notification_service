package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    @Captor
    private ArgumentCaptor<SendMessage> messageCaptor;

    @Test
    void testSend() throws TelegramApiException {
        UserDto userDto = UserDto.builder().id(1L).build();

        telegramService.send(userDto, "Hi");

        verify(telegramBot).execute(messageCaptor.capture());

        assertEquals(userDto.getId(), Long.parseLong(messageCaptor.getValue().getChatId()));
        assertEquals("Hi", messageCaptor.getValue().getText());
    }
}
