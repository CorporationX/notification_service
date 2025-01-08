package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
public class NotificationTelegramServiceTest {

    @Mock
    private TelegramBotService telegramBotService;

    @Mock
    private TelegramService telegramService;

    @Mock
    private ThreadPoolTaskExecutor telegramBotExecutor;

    @InjectMocks
    private NotificationTelegramService notificationTelegramService;

    @Test
    public void testGetPreferredContact() {
        UserDto.PreferredContact result = notificationTelegramService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.TELEGRAM, result, "Preferred contact should be TELEGRAM");
    }

    @Test
    void testSendMessageSuccessfully() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        String message = "Hello!";
        Long chatId = 123456L;


        CompletableFuture<Long> completableChatId = CompletableFuture.completedFuture(chatId);
        when(telegramService.findChatIdByUserId(user.getId())).thenReturn(completableChatId);

        notificationTelegramService.send(user, message);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotService, timeout(1000)).execute(messageCaptor.capture());

        SendMessage capturedMessage = messageCaptor.getValue();
        assertNotNull(capturedMessage);
        assertEquals(chatId.toString(), capturedMessage.getChatId());
        assertEquals(message, capturedMessage.getText());
    }

    @Test
    void testSendMessageWhenChatIdIsNull() throws Exception {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        String message = "Hello!";

        CompletableFuture<Long> completableChatId = CompletableFuture.completedFuture(null);
        when(telegramService.findChatIdByUserId(user.getId())).thenReturn(completableChatId);

        // Act
        notificationTelegramService.send(user, message);

        // Assert
        verify(telegramBotService, never()).execute(any(SendMessage.class));
        // Здесь вы можете также проверить вызов логирования с помощью библиотеки, например, LogCaptor
    }

    @Test
    void testSendMessageHandlesTelegramApiException() throws Exception {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        String message = "Hello!";
        Long chatId = 123456L;

        CompletableFuture<Long> completableChatId = CompletableFuture.completedFuture(chatId);
        when(telegramService.findChatIdByUserId(user.getId())).thenReturn(completableChatId);

        doThrow(new TelegramApiException("API Error"))
                .when(telegramBotService).execute(any(SendMessage.class));

        // Act
        notificationTelegramService.send(user, message);

        // Assert
        verify(telegramBotService, timeout(1000)).execute(any(SendMessage.class));
        // Также можно проверить логирование
    }

    @Test
    void testSendMessageHandlesInterruptedException() throws Exception {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        String message = "Hello!";

        CompletableFuture<Long> completableChatId = new CompletableFuture<>();
        completableChatId.completeExceptionally(new InterruptedException("Interrupted"));

        when(telegramService.findChatIdByUserId(user.getId())).thenReturn(completableChatId);

        notificationTelegramService.send(user, message);

        // Assert
        verify(telegramBotService, never()).execute(any(SendMessage.class));
    }

    @Test
    void testSendMessageHandlesExecutionException() throws Exception {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        String message = "Hello!";

        CompletableFuture<Long> completableChatId = new CompletableFuture<>();
        completableChatId.completeExceptionally(new ExecutionException("Execution failed", null));

        when(telegramService.findChatIdByUserId(user.getId())).thenReturn(completableChatId);

        // Act
        yourClass.send(user, message);

        // Assert
        verify(telegramBotService, never()).execute(any(SendMessage.class));
        // Также можно проверить логирование
    }

}
