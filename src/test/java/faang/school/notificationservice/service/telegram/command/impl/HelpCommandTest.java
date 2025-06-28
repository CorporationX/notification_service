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

class HelpCommandTest {
    private static final Long CHAT_ID = 1L;

    private MessageSource messageSource;
    private final Locale locale = new Locale("ru");
    private TelegramBot telegramBot;
    private HelpCommand helpCommand;

    @BeforeEach
    public void setUp() {
        telegramBot = Mockito.mock(TelegramBot.class);
        messageSource = Mockito.mock(MessageSource.class);
        helpCommand = new HelpCommand(messageSource, locale);
        helpCommand.setBot(telegramBot);
    }

    @Test
    public void testCreateCommand() {
        String expectedDescription = "help command description";
        when(messageSource.getMessage(eq(HelpCommand.DESCRIPTION), isNull(), eq(locale)))
            .thenReturn(expectedDescription);

        assertEquals(HelpCommand.COMMAND, helpCommand.getCommandName());
        assertEquals(expectedDescription, helpCommand.getDescription());
    }

    @Test
    public void testExecuteCommand() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(CHAT_ID);
        doNothing().when(telegramBot).sendCommandList(CHAT_ID);

        helpCommand.execute(update);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(telegramBot).sendCommandList(captor.capture());
        Long id = captor.getValue();
        assertEquals(CHAT_ID, id);
    }
}