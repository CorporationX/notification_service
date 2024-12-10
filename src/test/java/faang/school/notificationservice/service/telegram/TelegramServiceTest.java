package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.telegram.TelegramBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {
    @Mock
    private TelegramBot telegramBot;
    @InjectMocks
    private TelegramService telegramService;
    @Captor
    private ArgumentCaptor<SendMessage> messageCaptor;

    @Test
    void sendMessageSuccessTest() {
        UserDto user = UserDto.builder()
                .id(1)
                .username("Alex")
                .email("alex@mail.ru")
                .phone("+79061234567")
                .telegramId(864588922L)
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();

        telegramService.send(user, "Notification for Alex");
        verify(telegramBot).send(messageCaptor.capture());
        assertEquals(user.getTelegramId(), Long.parseLong(messageCaptor.getValue().getChatId()));
        assertEquals("Notification for Alex", messageCaptor.getValue().getText());
    }

    @Test
    void sendMessageWithNullTelegramIdFailTest() {
        UserDto user = UserDto.builder()
                .id(1)
                .username("Alex")
                .email("alex@mail.ru")
                .phone("+79061234567")
                .telegramId(null)
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> telegramService.send(user, "Notification for Alex"));
        String expectedMessage = "Unable to send notification to telegram. TelegramID is null for user: 1";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(telegramBot, never()).send(messageCaptor.capture());
    }

    @Test
    void sendMessageWithNotTelegramPreferenceContactFailTest() {
        UserDto user = UserDto.builder()
                .id(1)
                .username("Alex")
                .email("alex@mail.ru")
                .phone("+79061234567")
                .telegramId(864588922L)
                .preference(UserDto.PreferredContact.SMS)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> telegramService.send(user, "Notification for Alex"));
        String expectedMessage = "Telegram is not the preferred platform for sending notifications for user: 1";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(telegramBot, never()).send(messageCaptor.capture());
    }
}
