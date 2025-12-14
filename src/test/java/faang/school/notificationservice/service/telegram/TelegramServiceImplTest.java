package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceImplTest {

    private TelegramServiceImpl telegramService;

    @BeforeEach
    public void setUp() {
        telegramService = new TelegramServiceImpl("test_token") {
            @Override
            public String getBotUsername() {
                return "test_bot";
            }
        };
    }

    @Test
    public void send_shouldCallSendMessage() {
        UserDto dto = new UserDto();
        dto.setId(123L);
        dto.setContactPreference(UserDto.PreferredContact.TELEGRAM);
        String messageText = "Test";

        TelegramServiceImpl spyService = spy(telegramService);
        doNothing().when(spyService).send(dto, messageText);

        spyService.send(dto, messageText);

        verify(spyService).send(dto, messageText);
    }
}