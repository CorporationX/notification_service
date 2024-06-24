package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnknownCommandTest {

    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private TelegramProfileService telegramProfileService;
    @Mock
    private CommandBuilder commandBuilder;

    @InjectMocks
    private UnknownCommand unknownCommand;

    private final long chatId = 1L;
    private final String message = "message";
    private SendMessage sendMessage;

    @BeforeEach
    void setUp() {
        sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
    }

    @Test
    void sendMessage() {
        when(messageSource.getMessage(eq("telegram.unknown"), eq(null), any(Locale.class))).thenReturn(message);
        when(commandBuilder.buildMessage(chatId, message)).thenReturn(sendMessage);

        SendMessage actual = unknownCommand.sendMessage(chatId, message);
        assertEquals(sendMessage, actual);

        InOrder inOrder = inOrder(messageSource, commandBuilder, userServiceClient, telegramProfileService);
        inOrder.verify(messageSource).getMessage(eq("telegram.unknown"), eq(null), any(Locale.class));
        inOrder.verify(commandBuilder).buildMessage(chatId, message);
    }
}