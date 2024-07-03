package faang.school.notificationservice.service.bot;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Test
    public void sendMessage() {
        TelegramService telegramService = mock(TelegramService.class);
        UserDto user = UserDto.builder()
                .id(1L)
                .telegramChatId(77777777L)
                .build();
        String notification = "Hello!";

        ArgumentCaptor<UserDto> dtoArgumentCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<String> notifArgumentCaptor = ArgumentCaptor.forClass(String.class);


        doNothing().when(telegramService).send(dtoArgumentCaptor.capture(), notifArgumentCaptor.capture());

        telegramService.send(user, notification);

        verify(telegramService, times(1)).send(any(UserDto.class), anyString());
        assertEquals(user, dtoArgumentCaptor.getValue());
        assertEquals(notification, notifArgumentCaptor.getValue());
    }
}