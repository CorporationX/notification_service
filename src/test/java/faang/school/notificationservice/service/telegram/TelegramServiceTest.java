package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.repository.TelegramChatRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
public class TelegramServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private TelegramChatRepository telegramChatRepository;

    @Mock
    private ThreadPoolTaskExecutor telegramBotExecutor;

    @InjectMocks
    private TelegramService telegramService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizeUser_WhenUserExists() throws ExecutionException, InterruptedException {
        long chatId = 12345L;
        String phoneNumber = "1234567890";
        long userId = 1L;

        when(userServiceClient.findUserByPhone(phoneNumber)).thenReturn(userId);

        doNothing().when(telegramChatRepository).saveOrUpdateChatId(chatId, userId);

        CompletableFuture<String> result = telegramService.authorizeUser(chatId, phoneNumber);
        assertEquals("Authorization was successful, welcome!", result.get());

        verify(telegramChatRepository, times(1)).saveOrUpdateChatId(chatId, userId);
    }


    @Test
    void testFindChatIdByUserId_Success() throws Exception {
        long userId = 1L;
        long expectedChatId = 12345L;

        // Настройка мока
        when(telegramChatRepository.findChatIdByUserId(userId)).thenReturn(expectedChatId);

        // Вызов метода
        CompletableFuture<Long> result = telegramService.findChatIdByUserId(userId);

        // Убедитесь, что значение совпадает
        assertEquals(expectedChatId, result.get()); // .get() ожидает завершения CompletableFuture

        // Убедитесь, что метод репозитория был вызван
        verify(telegramChatRepository).findChatIdByUserId(userId);
    }
}

