package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserServiceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {
    private final TelegramService telegramService = new TelegramService();
    UserServiceDto userServiceDto = UserServiceDto.builder().build();
    @Test
    void testSend() {
        telegramService.send(userServiceDto, "Test message");
    }

    @Test
    void testGetPreferredContact() {
        UserServiceDto.PreferredContact preferredContact = telegramService.getPreferredContact();

        assertEquals(UserServiceDto.PreferredContact.TELEGRAM, preferredContact);
    }
}