package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationBotTest {
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private TelegramUserRepository telegramUserRepository;
    @InjectMocks
    private TelegramNotificationBot telegramNotificationBot;

    @Test
    void testOnUpdateReceivedShouldCallsExecute() throws InvocationTargetException, IllegalAccessException, TelegramApiException, NoSuchMethodException {
        Method sendMessageMethod = TelegramNotificationBot.class.getDeclaredMethod("sendMessage", long.class, String.class);
        sendMessageMethod.setAccessible(true);

        TelegramNotificationBot telegramNotificationBot = Mockito.spy(new TelegramNotificationBot(userServiceClient, telegramUserRepository));
        doAnswer(invocation -> null).when(telegramNotificationBot).execute(any(SendMessage.class));

        sendMessageMethod.invoke(telegramNotificationBot, 123456789L, "Some message");

        verify(telegramNotificationBot).execute(any(SendMessage.class));
    }

    @Test
    void testOnUpdateReceived() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArgumentCaptor<TelegramUser> telegramUserArgumentCaptor = ArgumentCaptor.forClass(TelegramUser.class);
        Method subscribeToNotificationsMethod = TelegramNotificationBot
                .class.getDeclaredMethod("subscribeToNotifications", String.class, long.class);
        subscribeToNotificationsMethod.setAccessible(true);

        when(userServiceClient.isUserExists(anyLong())).thenReturn(true);
        subscribeToNotificationsMethod.invoke(telegramNotificationBot, "123456789", 123456789L);
        verify(userServiceClient).isUserExists(123456789L);
        verify(telegramUserRepository).save(telegramUserArgumentCaptor.capture());
    }

    @Test
    void testSendNotification() {
        assertDoesNotThrow(() -> telegramNotificationBot.sendNotification(1L, "text"));
    }

    @Test
    void testGetBotUsername() {
        assertEquals("TelegramNotificationBot", telegramNotificationBot.getBotUsername());
    }

    @Test
    void testGetBotToken() {
        assertEquals("7063955480:AAFT1nYPcT1SAv8zVQj0Mz-3i2ln7PaegQg", telegramNotificationBot.getBotToken());
    }
}