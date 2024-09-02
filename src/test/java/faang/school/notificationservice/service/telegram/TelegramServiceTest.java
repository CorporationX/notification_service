package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.user.UserDto;
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
        userDto = UserDto.builder()
                .id(1L)
                .build();
        message = "Hello world!";
    }

    @Test
    public void testSend() {

        telegramService.send(userDto, message);

        verify(telegramBot).sendMessageByUserId(userDto.getId(), message);
    }

}