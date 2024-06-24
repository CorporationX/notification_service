package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.exception.NotFoundException;
import faang.school.notificationservice.repository.TelegramProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramProfileServiceImplTest {

    @Mock
    private TelegramProfileRepository telegramProfileRepository;
    
    @InjectMocks
    private TelegramProfileServiceImpl telegramProfileService;

    private final long userId = 1L;
    private final long chatId = 2L;
    private TelegramProfile telegramProfile;

    @BeforeEach
    void setUp() {
        telegramProfile = TelegramProfile.builder()
                .id(1L)
                .userId(1L)
                .chatId(chatId)
                .isActive(true)
                .build();
    }

    @Test
    void save() {
        when(telegramProfileRepository.save(telegramProfile)).thenReturn(telegramProfile);

        telegramProfileService.save(telegramProfile);

        InOrder inOrder = inOrder(telegramProfileRepository);
        inOrder.verify(telegramProfileRepository).save(telegramProfile);
    }

    @Test
    void findByUserId() {
        when(telegramProfileRepository.findByUserId(userId)).thenReturn(Optional.of(telegramProfile));

        TelegramProfile result = telegramProfileService.findByUserId(userId);
        assertEquals(telegramProfile, result);

        InOrder inOrder = inOrder(telegramProfileRepository);
        inOrder.verify(telegramProfileRepository).findByUserId(userId);
    }

    @Test
    void findByUserIdNotFoundException() {
        when(telegramProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> telegramProfileService.findByUserId(userId));
        assertEquals("Telegram profile with userId=" + userId + " not found", e.getMessage());
    }

    @Test
    void findByChatId() {
        when(telegramProfileRepository.findByChatId(chatId)).thenReturn(Optional.of(telegramProfile));

        Optional<TelegramProfile> result = telegramProfileService.findByChatId(chatId);
        assertEquals(telegramProfile, result.orElse(null));

        InOrder inOrder = inOrder(telegramProfileRepository);
        inOrder.verify(telegramProfileRepository).findByChatId(chatId);
    }

    @Test
    void existsByChatId() {
        when(telegramProfileRepository.existsByChatId(chatId)).thenReturn(true);

        boolean result = telegramProfileService.existsByChatId(chatId);
        assertTrue(result);

        InOrder inOrder = inOrder(telegramProfileRepository);
        inOrder.verify(telegramProfileRepository).existsByChatId(chatId);
    }
}