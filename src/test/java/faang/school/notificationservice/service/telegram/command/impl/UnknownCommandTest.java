package faang.school.notificationservice.service.telegram.command.impl;

import faang.school.notificationservice.service.telegram.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UnknownCommandTest {
    private static final String CHAT_ID = "1";

    private final UnknownCommand unknownCommand = new UnknownCommand();

    private TelegramBot telegramBot;

    @BeforeEach
    public void setUp() {
        telegramBot = Mockito.mock(TelegramBot.class);
        unknownCommand.setBot(telegramBot);
    }

    @Test
    public void testCreateCommand() {
        assertNull(unknownCommand.getCommandName());
        assertNull(unknownCommand.getDescription());
    }

    @Test
    public void testExecuteCommand() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(Long.parseLong(CHAT_ID));
        doNothing().when(telegramBot).sendMessage(eq(CHAT_ID), anyString());

        unknownCommand.execute(update);

        ArgumentCaptor<String> chatIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);
        verify(telegramBot).sendMessage(chatIdCaptor.capture(), textCaptor.capture());
        String id = chatIdCaptor.getValue();
        String actualText = textCaptor.getValue();
        String expectedText = UnknownCommand.UNKNOWN_TEXT;
        assertEquals(CHAT_ID, id);
        assertEquals(expectedText, actualText);
    }
}