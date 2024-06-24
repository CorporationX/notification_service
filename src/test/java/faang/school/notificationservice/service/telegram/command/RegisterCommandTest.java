package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactPreferenceDto;
import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import feign.FeignException;
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
class RegisterCommandTest {

    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private TelegramProfileService telegramProfileService;
    @Mock
    private CommandBuilder commandBuilder;

    @InjectMocks
    private RegisterCommand registerCommand;

    private final long chatId = 1L;
    private final String username = "username";
    private final String message = "message";
    private SendMessage sendMessage;
    private ContactPreferenceDto contactPreferenceDto;

    @BeforeEach
    void setUp() {
        sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        contactPreferenceDto = ContactPreferenceDto.builder()
                .id(1L)
                .userId(1L)
                .preference(PreferredContact.TELEGRAM)
                .build();
    }

    @Test
    void sendMessageAlreadyRegistered() {
        when(telegramProfileService.existsByChatId(chatId)).thenReturn(true);
        when(messageSource.getMessage(eq("telegram.start.already_registered"), eq(null), any(Locale.class))).thenReturn(message);
        when(commandBuilder.buildMessage(chatId, message)).thenReturn(sendMessage);

        SendMessage actual = registerCommand.sendMessage(chatId, username);
        assertEquals(sendMessage, actual);

        InOrder inOrder = inOrder(messageSource, commandBuilder, userServiceClient, telegramProfileService);
        inOrder.verify(telegramProfileService).existsByChatId(chatId);
        inOrder.verify(messageSource).getMessage(eq("telegram.start.already_registered"), eq(null), any(Locale.class));
        inOrder.verify(commandBuilder).buildMessage(chatId, message);
    }

    @Test
    void sendMessage() {
        when(telegramProfileService.existsByChatId(chatId)).thenReturn(false);
        when(userServiceClient.getContactPreference(username)).thenReturn(contactPreferenceDto);
        when(messageSource.getMessage(eq("telegram.start.registered"), eq(new String[]{username}), any(Locale.class))).thenReturn(message);
        when(commandBuilder.buildMessage(chatId, message)).thenReturn(sendMessage);

        SendMessage actual = registerCommand.sendMessage(chatId, username);
        assertEquals(sendMessage, actual);

        InOrder inOrder = inOrder(messageSource, commandBuilder, userServiceClient, telegramProfileService);
        inOrder.verify(telegramProfileService).existsByChatId(chatId);
        inOrder.verify(userServiceClient).getContactPreference(username);
        inOrder.verify(telegramProfileService).save(any(TelegramProfile.class));
        inOrder.verify(messageSource).getMessage(eq("telegram.start.registered"), eq(new String[]{username}), any(Locale.class));
        inOrder.verify(commandBuilder).buildMessage(chatId, message);
    }

    @Test
    void sendMessageFeignException() {
        when(telegramProfileService.existsByChatId(chatId)).thenReturn(false);
        when(userServiceClient.getContactPreference(username)).thenThrow(FeignException.class);
        when(messageSource.getMessage(eq("telegram.service_exception"), eq(null), any(Locale.class))).thenReturn(message);
        when(commandBuilder.buildMessage(chatId, message)).thenReturn(sendMessage);

        SendMessage actual = registerCommand.sendMessage(chatId, username);
        assertEquals(sendMessage, actual);

        InOrder inOrder = inOrder(messageSource, commandBuilder, userServiceClient, telegramProfileService);
        inOrder.verify(telegramProfileService).existsByChatId(chatId);
        inOrder.verify(userServiceClient).getContactPreference(username);
        inOrder.verify(messageSource).getMessage(eq("telegram.service_exception"), eq(null), any(Locale.class));
        inOrder.verify(commandBuilder).buildMessage(chatId, message);
    }
}