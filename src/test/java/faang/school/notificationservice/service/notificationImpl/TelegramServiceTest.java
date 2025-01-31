package faang.school.notificationservice.service.notificationImpl;

import faang.school.notificationservice.bot.TelegramBot;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.Language;
import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.service.notificationServiceImpl.TelegramService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @InjectMocks
    private TelegramService telegramService;
    @Mock
    private TelegramBot telegramBot;

    @Test
    public void testSend() {
        String message = "test message";
        String chatId = "123456789";
        ContactDto contactDto = ContactDto.builder()
                .contact(chatId)
                .type(ContactDto.ContactType.TELEGRAM)
                .userId(1L)
                .build();
        UserForNotificationDto user = UserForNotificationDto.builder()
                .id(1L)
                .contacts(List.of(contactDto))
                .locale(Language.EN)
                .build();

        telegramService.send(user, message);

        verify(telegramBot).sendTextMessage(Long.parseLong(chatId), message);
    }

    @Test
    public void testSendWithoutTelegramContact() {
        String message = "test message";
        UserForNotificationDto user = UserForNotificationDto.builder()
                .id(1L)
                .contacts(List.of())
                .locale(Language.EN)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> telegramService.send(user, message),
                "User telegram contact not found");

        verifyNoInteractions(telegramBot);
    }

}
