package faang.school.notificationservice.service.telegram.command.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HelpCommandTest {
    // public static final Long CHAT_ID = 1L;
    // public static final String TYPE = "group";

    // private Locale locale = new Locale("ru");
    // private MessageSource messageSource;
    // @Mock
    // TelegramBot telegramBot;
    // @InjectMocks
    // private HelpCommand helpCommand;

    @Test
    public void testCreateCommand() {
        // Update update = getUpdate();
        // helpCommand.setBot(telegramBot);

        // Mockito.doNothing().when(telegramBot).sendCommandList(anyLong());

        // helpCommand.execute(update);

        assertEquals("/help", HelpCommand.COMMAND);
        assertEquals("help.description", HelpCommand.DESCRIPTION);
        // ArgumentCaptor<HelpCommand> argumentCaptor = ArgumentCaptor.forClass(HelpCommand.class);
    }

    // private Update getUpdate() {
    //     Chat chat = new Chat(CHAT_ID, TYPE);
    //     Message message = new Message();
    //     message.setChat(chat);
    //     Update update = new Update();
    //     update.setMessage(message);
    //     return update;
    // }
}