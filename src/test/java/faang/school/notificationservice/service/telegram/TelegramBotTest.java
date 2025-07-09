package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.TelegramConfig;
import faang.school.notificationservice.exception.TelegramNotificationError;
import faang.school.notificationservice.service.TelegramService;
import faang.school.notificationservice.service.telegram.command.CommandContainer;
import faang.school.notificationservice.service.telegram.command.TelegramCommand;
import faang.school.notificationservice.service.telegram.command.impl.HelpCommand;
import faang.school.notificationservice.service.telegram.command.impl.RegisterCommand;
import faang.school.notificationservice.service.telegram.command.impl.UnregisterCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    public static final String CHAT_ID = "1";
    public static final Long USER_ID = 1L;
    public static final String PHONE = "1234567890";
    public static final String HELP_DESCRIPTION = "описание helpCommand";
    public static final String REGISTER_DESCRIPTION = "описание registerCommand";
    public static final String UNREGISTER_DESCRIPTION = "описание unregisterCommand";
    @Mock
    private TelegramConfig telegramConfig;
    @Spy
    private MessageSource messageSource;
    @Mock
    private TelegramService telegramService;
    @Mock
    private CommandContainer commandContainer;

    private TelegramMessages messages;

    private TelegramCommand helpCommand;
    private TelegramCommand registerCommand;
    private TelegramCommand unregisterCommand;

    private TelegramBot telegramBot;

    @BeforeEach
    public void setUp() throws Exception {
        Locale locale = new Locale("ru");

        messages = spy(new TelegramMessages(messageSource, locale));

        helpCommand = spy(new HelpCommand(messageSource, locale));
        registerCommand = spy(new RegisterCommand(messageSource, locale));
        unregisterCommand = spy(new UnregisterCommand(messageSource, locale));

        List<TelegramCommand> commandList = List.of();
        telegramBot = spy(new TelegramBot(telegramConfig, commandList, messages));
        telegramBot.setService(telegramService);

        // Используем рефлексию для замены CommandContainer нашим моком
        Field commandsField = TelegramBot.class.getDeclaredField("commands");
        commandsField.setAccessible(true);
        commandsField.set(telegramBot, commandContainer);
    }

    @Test
    public void testConstructorInitializesCorrectly() {
        assertNotNull(telegramBot);
    }

    @Test
    public void testGetBotName() {
        when(telegramConfig.getBotName()).thenReturn("bot-name");

        telegramBot.getBotUsername();

        verify(telegramBot).getBotUsername();
    }

    @Test
    public void testOnUpdateReceivedHelpCommand() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/help");
        when(commandContainer.getCommand(helpCommand.getCommandName())).thenReturn(helpCommand);
        doNothing().when(helpCommand).execute(update);

        telegramBot.onUpdateReceived(update);

        verify(helpCommand).execute(any(Update.class));
    }

    @Test
    public void testOnUpdateReceivedContactInfo() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Contact contact = Mockito.mock(Contact.class);
        User user = Mockito.mock(User.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(false);
        when(message.hasContact()).thenReturn(true);
        when(message.getContact()).thenReturn(contact);
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(contact.getUserId()).thenReturn(USER_ID);
        when(message.getChatId()).thenReturn(Long.parseLong(CHAT_ID));
        when(contact.getPhoneNumber()).thenReturn(PHONE);
        doNothing().when(telegramService).registerUser(CHAT_ID, PHONE);

        telegramBot.onUpdateReceived(update);

        verify(commandContainer, times(0)).getCommand(anyString());
        verify(telegramService).registerUser(CHAT_ID, PHONE);
    }

    @Test
    public void testOnUpdateReceivedFailContactInfo() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Contact contact = Mockito.mock(Contact.class);
        User user = Mockito.mock(User.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(false);
        when(message.hasContact()).thenReturn(true);
        when(message.getContact()).thenReturn(contact);
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(contact.getUserId()).thenReturn(USER_ID + 1);

        telegramBot.onUpdateReceived(update);

        verify(commandContainer, times(0)).getCommand(anyString());
        verify(telegramService, times(0)).registerUser(CHAT_ID, PHONE);
    }

    @Test
    public void testOnUpdateReceived_NoMessage() {
        Update update = Mockito.mock(Update.class);
        when(update.hasMessage()).thenReturn(false);

        telegramBot.onUpdateReceived(update);

        verify(commandContainer, times(0)).getCommand(anyString());
        verify(telegramService, times(0)).registerUser(CHAT_ID, PHONE);
    }

    @Test
    public void testOnUpdateReceived_MessageExistsButNoContact() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(false);
        when(message.hasContact()).thenReturn(false);

        telegramBot.onUpdateReceived(update);

        verify(commandContainer, times(0)).getCommand(anyString());
        verify(telegramService, times(0)).registerUser(CHAT_ID, PHONE);
    }

    @Test
    public void testSuccessfulSendContactRequest() {
        when(messages.get(TelegramLabel.REGISTER_PHONE)).thenReturn("REGISTER_PHONE");
        when(messages.get(TelegramLabel.REGISTER_USER_IN_APP)).thenReturn("REGISTER_USER_IN_APP");
        doNothing().when(telegramBot).innerExecute(any(SendMessage.class), anyString());

        telegramBot.sendContactRequest(CHAT_ID);

        verify(telegramBot).innerExecute(any(SendMessage.class), anyString());
    }

    @Test
    public void testFailSendContactRequest() {
        String errorMessage = "Telegram Notification Error";
        TelegramNotificationError telegramNotificationError = new TelegramNotificationError(errorMessage);

        when(messages.get(TelegramLabel.REGISTER_PHONE)).thenReturn("REGISTER_PHONE");
        when(messages.get(TelegramLabel.REGISTER_USER_IN_APP)).thenReturn("REGISTER_USER_IN_APP");
        doThrow(telegramNotificationError).when(telegramBot).innerExecute(any(SendMessage.class), anyString());

        TelegramNotificationError exception = assertThrows(TelegramNotificationError.class,
            () -> telegramBot.sendContactRequest(CHAT_ID));

        assertEquals(telegramNotificationError.getMessage(), exception.getMessage());
    }


    @Test
    public void testSuccessfulRegisterMenu() {
        Map<String, TelegramCommand> commandMap = getTelegramCommandsMap();
        when(commandContainer.getCommands()).thenReturn(commandMap);
        when(helpCommand.getDescription()).thenReturn(HELP_DESCRIPTION);
        when(registerCommand.getDescription()).thenReturn(REGISTER_DESCRIPTION);
        when(unregisterCommand.getDescription()).thenReturn(UNREGISTER_DESCRIPTION);
        doNothing().when(telegramBot).innerExecute(any(SetMyCommands.class), anyString());

        telegramBot.registerCommands();

        ArgumentCaptor<SetMyCommands> captor = ArgumentCaptor.forClass(SetMyCommands.class);
        verify(telegramBot).innerExecute(captor.capture(), anyString());
        SetMyCommands setMyCommands = captor.getValue();
        List<BotCommand> botCommands = setMyCommands.getCommands();

        assertEquals(3, botCommands.size());
    }

    @Test
    public void testSendCommandList() {
        Map<String, TelegramCommand> commandMap = getTelegramCommandsMap();
        when(messages.get(TelegramLabel.COMMAND_LIST)).thenReturn("COMMAND_LIST");
        when(commandContainer.getCommands()).thenReturn(commandMap);
        when(helpCommand.getDescription()).thenReturn(HELP_DESCRIPTION);
        when(registerCommand.getDescription()).thenReturn(REGISTER_DESCRIPTION);
        when(unregisterCommand.getDescription()).thenReturn(UNREGISTER_DESCRIPTION);
        doNothing().when(telegramBot).innerExecute(any(SendMessage.class), anyString());

        telegramBot.sendCommandList(CHAT_ID);

        verify(telegramBot).innerExecute(any(SendMessage.class), anyString());
    }

    @Test
    public void testUnregisterChatId() {
        doNothing().when(telegramService).unregisterUser(CHAT_ID);

        telegramBot.unregisterChatId(CHAT_ID);

        verify(telegramService).unregisterUser(CHAT_ID);
    }

    @Test
    public void testRemoveKeyboardAfterReceivingContact() {
        String expectedText = "expected_message_text";
        doNothing().when(telegramBot).innerExecute(any(SendMessage.class), anyString());

        telegramBot.removeKeyboardAfterReceivingContact(CHAT_ID, expectedText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).innerExecute(captor.capture(), anyString());
        SendMessage sendMessage = captor.getValue();
        ReplyKeyboardRemove removeKeyboard = (ReplyKeyboardRemove) sendMessage.getReplyMarkup();

        assertEquals(expectedText, sendMessage.getText());
        assertEquals(CHAT_ID, sendMessage.getChatId());
        assertTrue(removeKeyboard.getRemoveKeyboard());
    }

    private Map<String, TelegramCommand> getTelegramCommandsMap() {
        return Map.of(
            "helpCommand", helpCommand,
            "registerCommand", registerCommand,
            "unregisterCommand", unregisterCommand
        );
    }
}