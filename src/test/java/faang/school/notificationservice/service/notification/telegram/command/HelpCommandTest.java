package faang.school.notificationservice.service.notification.telegram.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest extends CommandTest {

    @InjectMocks
    private HelpCommand helpCommand;

    @BeforeEach
    void setUp() {
        helpCommand = new HelpCommand(messageSource, telegramProfilesService, userServiceClient);
    }

    @Test
    void testExecute() {
        Mockito.when(messageSource.getMessage("telegram.help", new Object[]{"setooooon"}, defaultLocale))
                .thenReturn("test");

        SendMessage actual = helpCommand.execute(1L, "setooooon");

        assertEquals("test", actual.getText());
        assertEquals("1", actual.getChatId());
    }
}