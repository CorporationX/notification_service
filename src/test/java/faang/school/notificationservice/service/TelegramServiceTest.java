package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import faang.school.notificationservice.telegram.TelegramBot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {
    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    private UserDto userDto;
    private String message;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(123L)
                .build();
        message = "Test message";
    }

    @Test
    void testSendTelegramMessageSuccessful() {
        telegramService.send(userDto, message);

        verify(telegramBot, times(1)).sendMessage(userDto.getId(), message);
    }

    @Test
    void testGetPreferredContactSuccessful() {
        UserDto.PreferredContact preferredContact = telegramService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.TELEGRAM, preferredContact);
    }
}
