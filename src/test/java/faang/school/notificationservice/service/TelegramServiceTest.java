package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private TelegramClient telegramClient;

    @InjectMocks
    private TelegramService telegramService;

    @Test
    void testSend() throws TelegramApiException {
        UserDto user = UserDto.builder()
                .tgChatId(11L)
                .build();
        String message = "message";
        SendMessage tgMessage = SendMessage.builder()
                .chatId(user.getTgChatId())
                .text(message)
                .build();

        telegramService.send(user, message);

        Mockito.verify(telegramClient, Mockito.times(1)).execute(tgMessage);
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = telegramService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.TELEGRAM, preferredContact);
    }
}