package faang.school.notificationservice.service;

import faang.school.notificationservice.config.telegram.TelegramBot;
import faang.school.notificationservice.dto.UserNotificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {
    @Mock
    private TelegramBot telegramBot;
    @InjectMocks
    private TelegramService telegramService;

    @Test
    void send_ShouldCallTelegramBotWithCorrectParameters() {
        UserNotificationDto user = new UserNotificationDto();
        user.setChatId(123456L);
        String message = "Test message";

        telegramService.send(user, message);

        verify(telegramBot, times(1)).sendMessage(123456L, message);
    }

    @Test
    void getPreferredContact_ShouldReturnTelegram() {
        UserNotificationDto.PreferredContact contact = telegramService.getPreferredContact();

        assertEquals(UserNotificationDto.PreferredContact.TELEGRAM, contact);
    }
}