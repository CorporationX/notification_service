package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.entity.TelegramProfile;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private TelegramProfileService telegramProfileService;
    @Mock
    private CommandBuilder commandBuilder;

    @InjectMocks
    private StartCommand startCommand;

    private final long chatId = 1L;
    private final String username = "username";
    private final String message = "message";
    private SendMessage sendMessage;
    private TelegramProfile telegramProfile;

    @BeforeEach
    void setUp() {
        sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        telegramProfile = TelegramProfile.builder()
                .id(1L)
                .userId(1L)
                .chatId(chatId)
                .isActive(true)
                .build();
    }

    @Test
    void sendMessageNotRegisteredUser() {
        when(telegramProfileService.findByChatId(chatId)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("telegram.not_registered_corporationX"), eq(null), any(Locale.class))).thenReturn(message);
        when(commandBuilder.buildMessage(chatId, message)).thenReturn(sendMessage);

        SendMessage actual = startCommand.sendMessage(chatId, username);
        assertEquals(sendMessage, actual);

        InOrder inOrder = inOrder(messageSource, userServiceClient, telegramProfileService, commandBuilder);
        inOrder.verify(telegramProfileService).findByChatId(chatId);
        inOrder.verify(messageSource).getMessage(eq("telegram.not_registered_corporationX"), eq(null), any(Locale.class));
        inOrder.verify(commandBuilder).buildMessage(chatId, message);
    }

    @Test
    void sendMessage() {
        when(telegramProfileService.findByChatId(chatId)).thenReturn(Optional.of(telegramProfile));
        when(messageSource.getMessage(eq("telegram.stop"), eq(new String[]{username}), any(Locale.class))).thenReturn(message);
        when(commandBuilder.buildMessage(chatId, message)).thenReturn(sendMessage);

        SendMessage actual = startCommand.sendMessage(chatId, username);
        assertFalse(telegramProfile.isActive());
        assertEquals(sendMessage, actual);

        InOrder inOrder = inOrder(messageSource, userServiceClient, telegramProfileService, commandBuilder);
        inOrder.verify(telegramProfileService).findByChatId(chatId);
        inOrder.verify(telegramProfileService).save(telegramProfile);
        inOrder.verify(messageSource).getMessage(eq("telegram.stop"), eq(new String[]{username}), any(Locale.class));
        inOrder.verify(commandBuilder).buildMessage(chatId, message);
    }
}