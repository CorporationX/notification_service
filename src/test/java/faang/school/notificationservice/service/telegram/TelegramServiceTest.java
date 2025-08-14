package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.TELEGRAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private TelegramBot telegramBot;

    private static final String userName = "Vald";
    private static final String phone = "+79851111865";
    private static final String email = "Ya@ya.ru";
    private static final long userId = 1234;
    private static final String chatId = "1234";

    @InjectMocks
    private TelegramService telegramService;


    @Test
    @DisplayName("Should send message successfully when input is valid")
    void shouldSendSmsSuccessfully() {
        UserDto user = createUserDto();

        String message = "Hello";
        telegramService.send(user, message);

        verify(telegramBot).sendMessage(chatId, message);
    }

    @Test
    @DisplayName("Should return preferred contact type as TELEGRAM")
    void shouldReturnPreferredContactAsPhone() {
        assertEquals(TELEGRAM, telegramService.getPreferredContact());
    }

    private UserDto createUserDto() {
        UserDto user = new UserDto();
        user.setId(userId);
        user.setUsername(userName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPreference(TELEGRAM);
        return user;
    }
}
