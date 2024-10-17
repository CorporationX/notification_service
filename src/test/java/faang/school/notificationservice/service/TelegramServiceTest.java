package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.telegram.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    @Captor
    private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    private Long telegramId;
    private String message;
    private UserDto user;

    @BeforeEach
    void setUp() {
        telegramId = 123456789L;
        message = "Test message";
        user = UserDto.builder()
                .telegramId(telegramId)
                .build();
    }

    @Test
    void testSend_successful() throws TelegramApiException {
        SendMessage correctResult = SendMessage.builder()
                .chatId(telegramId)
                .text(message)
                .build();

        telegramService.send(user, message);

        verify(telegramBot).executeAsync(sendMessageArgumentCaptor.capture());
        SendMessage result = sendMessageArgumentCaptor.getValue();
        assertEquals(correctResult, result);
    }

    @Test
    void testSend_failed() throws TelegramApiException {
        String errorMessage = "Test exception";
        doThrow(new TelegramApiException(errorMessage))
                .when(telegramBot).executeAsync(any(SendMessage.class));

        telegramService.send(user, message);

        verify(telegramBot).executeAsync(any(SendMessage.class));
    }

    @Test
    void testGetPreferredContact() {
        assertEquals(UserDto.PreferredContact.TELEGRAM, telegramService.getPreferredContact());
    }
}
