package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import faang.school.notificationservice.service.telegram.command.impl.UnknownCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandContainerTest {

    public static final String FIRST_COMMAND = "firstCommand";
    public static final String SECOND_COMMAND = "secondCommand";
    private CommandContainer commandContainer;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private TelegramCommand firstCommand;
    @Mock
    private TelegramCommand secondCommand;


    @BeforeEach
    public void setUp() {
        List<TelegramCommand> commandList = new ArrayList<>();
        commandList.add(firstCommand);
        commandList.add(secondCommand);

        when(firstCommand.getCommandName()).thenReturn(FIRST_COMMAND);
        when(secondCommand.getCommandName()).thenReturn(SECOND_COMMAND);

        commandContainer = new CommandContainer(telegramBot, commandList);
    }

    @Test
    public void testCreateCommandContainer() {
        assertNotNull(commandContainer);
        assertEquals(2, commandContainer.getCommands().size());
    }

    @Test
    public void testGetExistsCommand() {
        assertEquals(firstCommand, commandContainer.getCommand(FIRST_COMMAND));
    }

    @Test
    public void testFailGetCommand() {
        assertEquals(UnknownCommand.class, commandContainer.getCommand("missing").getClass());
    }

    @Test
    public void testToString() {
        assertNotNull(commandContainer.toString());
    }
}