package faang.school.notificationservice;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
import faang.school.notificationservice.service.impl.TelegramService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private TelegramUserRepository telegramUserRepository;

    @InjectMocks
    private TelegramService telegramService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendSuccess() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(1L);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setTelegramUserId(123456789L);
        telegramUser.setUserName("TestUser");
        when(telegramUserRepository.findByUserId(1L)).thenReturn(Optional.of(telegramUser));

        telegramService.send(user, "Hello!");

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void testSendUserNotFound() throws TelegramApiException {
        UserDto user = new UserDto();
        user.setId(1L);
        when(telegramUserRepository.findByUserId(1L)).thenReturn(Optional.empty());

        try {
            telegramService.send(user, "Hello!");
        } catch (EntityNotFoundException e) {
            assertEquals("Telegram user with id = 1 not found", e.getMessage());
        }

        verify(telegramBot, never()).execute(any(SendMessage.class));
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact result = telegramService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.TELEGRAM, result);
    }
}
