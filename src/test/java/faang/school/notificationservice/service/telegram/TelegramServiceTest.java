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

//    @Test
//    void testAuthorizeUser_WhenUserExists() throws ExecutionException, InterruptedException {
//        long chatId = 12345L;
//        String phoneNumber = "1234567890";
//        long userId = 1L;
//
//        // Настроим мок для UserServiceClient
//        when(userServiceClient.findUserByPhone(phoneNumber)).thenReturn(userId);
//
//        // Мокаем сохранение chatId в репозитории
//        doNothing().when(telegramChatRepository).saveOrUpdateChatId(chatId, userId);
//
//        // Имитируем выполнение CompletableFuture
//        CompletableFuture<String> result = CompletableFuture.completedFuture("Authorization was successful, welcome!");
//
//        // Вызов тестируемого метода
//        String message = result.get(); // Это имитирует успешное выполнение задачи
//
//        assertEquals("Authorization was successful, welcome!", message);
//        verify(telegramChatRepository, times(1)).saveOrUpdateChatId(chatId, userId);
//    }
//
//
//    @Test
//    void testFindChatIdByUserId_Success() throws Exception {
//    }
}

