package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserProfileDto;
import faang.school.notificationservice.service.telegram.TelegramNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private TelegramNotificationService telegramService;
    private final String expectedUrl = "https://api.telegram.org/bottest_token/sendMessage";

    @BeforeEach
    void setUp() {
        telegramService = new TelegramNotificationService(restTemplate, "test_token");
    }

    @Test
    void testSendTelegramMessage() {
        UserProfileDto user = new UserProfileDto();
        user.setId(1L);
        user.setTelegramChatId(12345L);
        String message = "Test message";

        telegramService.send(user, message);

        verify(restTemplate).postForObject(
                eq(expectedUrl),
                argThat(request ->
                        request.toString().contains("chat_id=12345") &&
                                request.toString().contains("text=Test message")
                ),
                eq(String.class)
        );
    }
}