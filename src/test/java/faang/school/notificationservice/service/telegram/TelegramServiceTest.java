package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {
    @Mock
    private TelegramNotificationBot telegramNotificationBot;
    @Mock
    private TelegramUserRepository telegramUserRepository;
    @InjectMocks
    private TelegramService telegramService;

    @Test
    void testGetPreferredContact() {
        assertEquals(UserDto.PreferredContact.TELEGRAM, telegramService.getPreferredContact());
    }

    @Test
    void testSend() {
        UserDto userDto = UserDto.builder().id(1L).build();
        String message = "Test message";
        TelegramUser telegramUser = new TelegramUser();
        when(telegramUserRepository.findByUserId(anyLong())).thenReturn(Optional.of(telegramUser));
        telegramService.send(userDto, message);
        verify(telegramNotificationBot, times(1)).sendNotification(anyLong(), anyString());
    }
}