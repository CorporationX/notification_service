package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.TelegramId;
import faang.school.notificationservice.entity.User;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.repository.TelegramIdRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private NotificationBot notificationBot;

    @Mock
    private TelegramIdRepository telegramIdRepository;

    @InjectMocks
    private TelegramService telegramService;

    private UserDto user;
    private final Long chatId = 12345L;
    private final String message = "Test message";

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setUsername("testUser");
    }

    @Test
    void sendShouldUseNotificationBot() {
        when(telegramIdRepository.findByUserId(user.getId())).thenReturn(Optional.of(new TelegramId(1L, new User(), chatId)));

        telegramService.send(user, message);

        verify(notificationBot).sendMessage(chatId, message);
    }

    @Test
    void sendShouldThrowExceptionWhenChatIdNotFound() {
        when(telegramIdRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        DataValidationException dataValidationException = assertThrows(DataValidationException.class, () -> telegramService.send(user, message));

        assertEquals("Не зарегистрирован chat_id", dataValidationException.getMessage());
    }
}
