package faang.school.notificationservice.service.telegram.command.impl;

import faang.school.notificationservice.service.telegram.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UnregisterCommandTest {
    private static final String CHAT_ID = "1";

    private MessageSource messageSource;
    private final Locale locale = new Locale("ru");
    private TelegramBot telegramBot;
    private UnregisterCommand unregisterCommand;

    @BeforeEach
    public void setUp() {
        telegramBot = Mockito.mock(TelegramBot.class);
        messageSource = Mockito.mock(MessageSource.class);
        unregisterCommand = new UnregisterCommand(messageSource, locale);
        unregisterCommand.setBot(telegramBot);
    }

    @Test
    public void testCreateCommand() {
        String expectedDescription = "register command description";
        when(messageSource.getMessage(eq(UnregisterCommand.DESCRIPTION), isNull(), eq(locale)))
            .thenReturn(expectedDescription);

        assertEquals(UnregisterCommand.COMMAND, unregisterCommand.getCommandName());
        assertEquals(expectedDescription, unregisterCommand.getDescription());
    }


    @Test
    public void testExecuteCommand() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(Long.parseLong(CHAT_ID));
        doNothing().when(telegramBot).unregisterChatId(CHAT_ID);

        unregisterCommand.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(telegramBot).unregisterChatId(captor.capture());
        String id = captor.getValue();
        assertEquals(CHAT_ID, id);
    }
}