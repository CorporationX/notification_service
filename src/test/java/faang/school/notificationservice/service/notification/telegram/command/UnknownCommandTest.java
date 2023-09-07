package faang.school.notificationservice.service.notification.telegram.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

class UnknownCommandTest extends CommandTest {

    @InjectMocks
    private UnknownCommand unknownCommand;

    @BeforeEach
    void setUp() {
        unknownCommand = new UnknownCommand(messageSource, telegramProfilesService, userServiceClient);
    }

    @Test
    void testExecute() {
        Mockito.when(messageSource.getMessage("telegram.unknown", null, defaultLocale))
                .thenReturn("test");

        SendMessage actual = unknownCommand.execute(1L, "setooooon");

        assertEquals("test", actual.getText());
        assertEquals("1", actual.getChatId());
    }
}