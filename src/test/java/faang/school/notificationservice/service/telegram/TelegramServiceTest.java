package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramService telegramService;

    private UserDto userDto;
    private String message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        telegramService = new TelegramService(telegramBot);
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setTelegramId(12345678L);
        message = "Hello world!";
    }

    @Test
    public void testSend() {

        telegramService.send(userDto, message);

        verify(telegramBot).sendMessageByUserId(userDto.getTelegramId(), message);
    }

}