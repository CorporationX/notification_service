package faang.school.notificationservice.service.telegram.command;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnknownCommandTest extends CommandTest {

    @InjectMocks
    private UnknownCommand unknownCommand;

    @Test
    void testExecute() {
        long chatId = 1L;

        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn("Unknown command");
        SendMessage message = unknownCommand.execute(chatId, "name");

        assertEquals("Unknown command", message.getText());
        assertEquals(String.valueOf(chatId), message.getChatId());
    }
}
