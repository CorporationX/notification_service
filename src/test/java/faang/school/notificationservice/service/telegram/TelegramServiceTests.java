package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
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
public class TelegramServiceTests {

    @InjectMocks
    private TelegramService telegramService;

    @Mock
    private TelegramBot telegramBot;

    @Captor
    private ArgumentCaptor<SendMessage> messageCaptor;

    @Test
    void testSend() throws TelegramApiException {
        UserDto userDto = UserDto.builder().id(1L).build();

        telegramService.send(userDto, "Message");

        verify(telegramBot).execute(messageCaptor.capture());

        assertEquals(userDto.getId(), Long.parseLong(messageCaptor.getValue().getChatId()));
        assertEquals("Message", messageCaptor.getValue().getText());
    }
}