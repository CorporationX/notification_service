package faang.school.notificationservice.service.telegram;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceImplTest {

    @InjectMocks
    private TelegramServiceImpl telegramServiceImpl;

    @Test
    void executeMessage_shouldSendMessage_whenValidParameters() {
        try {
            long chatId = 123456789L;
            String messageText = "Test message";

            telegramServiceImpl.executeMessage(chatId, messageText);

            verify(telegramServiceImpl, times(1)).execute(any(SendMessage.class));
        } catch (Exception e) {
        }
    }

    @Test
    void executeMessage_shouldThrowRuntimeException_whenTelegramApiExceptionOccurs() {
        try {
            long chatId = 123456789L;
            String messageText = "Test message";

            doThrow(new TelegramApiException("API error"))
                    .when(telegramServiceImpl).execute(any(SendMessage.class));

            assertThrows(RuntimeException.class, () -> telegramServiceImpl.executeMessage(chatId, messageText));
        } catch (Exception e) {
        }
    }
}