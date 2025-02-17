package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramBotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotServiceTest {

    @Mock
    private TelegramClient telegramClient;

    @Mock
    private TelegramBotsLongPollingApplication botsApplication;

    private TelegramBotService telegramBotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        telegramBotService = new TelegramBotService("test-bot-token");

        ReflectionTestUtils.setField(telegramBotService, "telegramClient", telegramClient);
        ReflectionTestUtils.setField(telegramBotService, "botsApplication", botsApplication);
    }

    @Test
    void testSendMessage() throws TelegramApiException {
        UserDto user = UserDto.builder()
                .email("test@mail.com")
                .id(1L)
                .phone("+71234567878")
                .telegramChatId("123456")
                .username("testuser")
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();
        String message = "Test Message";

        when(telegramClient.execute(any(SendMessage.class))).thenReturn(null);

        telegramBotService.send(user, message);

        verify(telegramClient, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void testSendMessageThrowsException() throws TelegramApiException {
        UserDto user = UserDto.builder()
                .email("test@mail.com")
                .id(1L)
                .phone("+71234567878")
                .telegramChatId("123456")
                .username("testuser")
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();
        String message = "Test Message";

        doThrow(new TelegramApiException("Error"))
                .when(telegramClient).execute(any(SendMessage.class));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            telegramBotService.send(user, message);
        });
    }
}

